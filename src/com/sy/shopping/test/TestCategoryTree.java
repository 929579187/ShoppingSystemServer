package com.sy.shopping.test;

import com.alibaba.fastjson.JSON;
import com.sy.shopping.constant.AppConstant;
import com.sy.shopping.constant.BusinessConstant;
import com.sy.shopping.dao.CategoryDao;
import com.sy.shopping.dao.impl.CategoryDaoImpl;
import com.sy.shopping.dto.CategoryTreeNode;
import com.sy.shopping.entity.Category;
import com.sy.shopping.utils.RedisUtils;
import org.apache.commons.lang.StringUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TestCategoryTree {
    private static CategoryDao categoryDao = new CategoryDaoImpl();

    public static void main(String[] args) throws SQLException {


        //缓存：内存中的一块区域，对于一些变化频率不高的数据，不需要每次查询都到数据库来进行
        //实际的查询操作，例如此处的分类信息，查询过程复杂，如果每次查询都会浪费很多时间
        //但是分类信息对应访问网站的用户而言一般是不会发生变化的，所以可以使用缓存来存储这一块数据
        //当前端发起请求的时候先到缓存中进行查询，如果缓存中没有就到数据库进行查询，并将
        //查询到的数据放入缓存。
        //如果查询过程中在缓存中能够直接获取到对应的数据，那就直接返回，不用导数据库进行查询
        //从而节省开销

        List<CategoryTreeNode> categoryTreeNodes;
        //1.先从Redis中获取对应的分类信息
        String categoryJsonStr = RedisUtils.get(BusinessConstant.REDIS_CATEGORY_KEY);
        //(1-1)如果有
        if (StringUtils.isNotBlank(categoryJsonStr)) {
            System.out.println("从缓存中获取数据...");
            categoryTreeNodes = JSON.parseArray(categoryJsonStr, CategoryTreeNode.class);
        }
        //(2-2)如果没有
        else {
            System.out.println("从数据库中获取数据...");
            //以及分类对应的列表信息
            List<CategoryTreeNode> firstLevelCategoryNodeList = new ArrayList<>();

            //1.先获取所有的一节分类信息
            List<Category> firstLevelCategoryList = categoryDao.getCategoriesByParentId(BusinessConstant.FIRST_LEVEL_CATEGORY_PARENT_ID);
            //2.把一级分类对应的Category集合转换为CategoryTreeNode集合，因为在CategoryTreeNode中
            //有下面子级别的列表
            for (Category firstLevelCategory : firstLevelCategoryList) {
                Integer firstLevelCategoryId = firstLevelCategory.getId();
                String firstLevelCategoryName = firstLevelCategory.getName();

                CategoryTreeNode firstLevelCategoryNode = new CategoryTreeNode();
                firstLevelCategoryNode.setId(firstLevelCategoryId);
                firstLevelCategoryNode.setName(firstLevelCategoryName);
                //设置一级分类的子节点信息（也就是二级分类）
                //以当前一级分类的id作为父id查询得到的就是二级分类
                List<CategoryTreeNode> secondLevelCategoryNodeList = new ArrayList<>();
                //查询当前一级分类的二级分类
                List<Category> secondLevelCategoryList = categoryDao.getCategoriesByParentId(firstLevelCategoryId);
                //将二级分类对应的Category集合转换为对应的CategoryTreeNode集合
                for (Category secondLevelCategory : secondLevelCategoryList) {
                    Integer secondLevelCategoryId = secondLevelCategory.getId();
                    String secondLevelCategoryName = secondLevelCategory.getName();

                    CategoryTreeNode secondLevelCategoryNode = new CategoryTreeNode();
                    secondLevelCategoryNode.setId(secondLevelCategoryId);
                    secondLevelCategoryNode.setName(secondLevelCategoryName);

                    //设置三级节点信息
                    List<CategoryTreeNode> thirdLevelCategoryNodeList = new ArrayList<>();
                    List<Category> thirdLevelCategoryList = categoryDao.getCategoriesByParentId(secondLevelCategoryId);
                    for (Category thirdLevelCategory : thirdLevelCategoryList) {
                        Integer thirdLevelCategoryId = thirdLevelCategory.getId();
                        String thirdLevelCategoryName = thirdLevelCategory.getName();

                        CategoryTreeNode thirdLevelCategoryNode = new CategoryTreeNode();
                        thirdLevelCategoryNode.setId(thirdLevelCategoryId);
                        thirdLevelCategoryNode.setName(thirdLevelCategoryName);
                        thirdLevelCategoryNodeList.add(thirdLevelCategoryNode);
                    }
                    secondLevelCategoryNode.setChildNodes(thirdLevelCategoryNodeList);
                    secondLevelCategoryNodeList.add(secondLevelCategoryNode);

                }
                firstLevelCategoryNode.setChildNodes(secondLevelCategoryNodeList);
                firstLevelCategoryNodeList.add(firstLevelCategoryNode);
            }
            categoryTreeNodes = firstLevelCategoryNodeList;
            //如果是从数据库获取的，要将结果写入Redis
            //在Redis中保存对象的方式：
            //1.序列化、反序列化
            //2.JSON
            RedisUtils.set(BusinessConstant.REDIS_CATEGORY_KEY, JSON.toJSONString(firstLevelCategoryNodeList));
        }

    }
}
