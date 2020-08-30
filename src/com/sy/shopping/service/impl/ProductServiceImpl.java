package com.sy.shopping.service.impl;

import com.sy.shopping.constant.BusinessConstant;
import com.sy.shopping.dao.ProductDao;
import com.sy.shopping.dao.impl.ProductDaoImpl;
import com.sy.shopping.dto.Page;
import com.sy.shopping.dto.ProductClickCountInfo;
import com.sy.shopping.entity.Product;
import com.sy.shopping.entity.User;
import com.sy.shopping.exception.BusinessException;
import com.sy.shopping.service.ProductService;
import com.sy.shopping.service.UserService;
import com.sy.shopping.utils.CommonUtils;
import com.sy.shopping.utils.HttpUtils;
import com.sy.shopping.utils.RedisUtils;
import com.sy.shopping.vo.ProductQueryParam;
import org.apache.commons.lang.StringUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProductServiceImpl implements ProductService {
    private ProductDao productDao = new ProductDaoImpl();
    private UserService userService = new UserServiceImpl();

    @Override
    public boolean syncProductsClickCountToRedis() {
        List<ProductClickCountInfo> productClickCountInfoList = new ArrayList<>();
        try {
            //1.先获取所有的点击量数据
            productClickCountInfoList = productDao.getAllClickCountInfoList();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        //2.将集合转换为可以写入hash类型的Map集合
        Map<String, String> clickCountMap = new LinkedHashMap<>();
        for (ProductClickCountInfo productClickCountInfo : productClickCountInfoList) {
            clickCountMap.put(String.valueOf(productClickCountInfo.getId()),
                    String.valueOf(productClickCountInfo.getClickCount()));
        }
        //3.将所有的点击量数据写入Redis中
        return RedisUtils.hmset(BusinessConstant.REDIS_CLICK_COUNT_KEY, clickCountMap);
    }

    @Override
    public boolean syncProductsClickCountToMySQL() {
        //1.从Redis中读取所有的最新的点击量数据
        Map<String, String> redisClickCountMap = RedisUtils.hgetall(BusinessConstant.REDIS_CLICK_COUNT_KEY);
        //2.对Map进行循环，循环中将点击量数据同步到MySQL中
        for (Map.Entry<String, String> redisClickCountEntry : redisClickCountMap.entrySet()) {
            int pid = Integer.parseInt(redisClickCountEntry.getKey());
            int clickCount = Integer.parseInt(redisClickCountEntry.getValue());
            try {
                productDao.updateClickCount(pid, clickCount);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        return true;
    }

    @Override
    public Product getProductById(Integer pid) {
        Product product;
        try {
            product = productDao.getProductById(pid);
            if (null == product) {
                throw new BusinessException("该商品可能已经下架了");
            } else {
                //1.点击量+1操作
                //当该商品存在的时候对它的数量加1
                RedisUtils.hincrby(BusinessConstant.REDIS_CLICK_COUNT_KEY, String.valueOf(pid), BusinessConstant.REDIS_CLICK_COUNT_INCR);

                //2.添加浏览记录
                //两个情景：(1)用户未登录时候浏览(2)用户登录以后去浏览
                User user = userService.getLoginUser();
                //(1)用户未登录的时候浏览
                //首先在客户端生成一个唯一标识，用于标识当前的客户端
                //在Redis中与之对应通过一个List类型来存储当前客户端访问商品的id
                //如果同一个商品被访问多次，则后续的访问操作应该把这个id靠前,并且删除原来List中的这个id
                if (null == user) {
                    //获取Cookie中用于标识临时访问的Cookie的值
                    String cookieTempHistory = HttpUtils.getCookie(BusinessConstant.COOKIE_TEMP_HISTORY_NAME);
                    //如果Cookie中不存在
                    if (StringUtils.isBlank(cookieTempHistory)) {
                        //生成一个标识
                        String tempHistoryCookieValue = CommonUtils.generateUuid();
                        //将这个标识写入Cookie
                        HttpUtils.setCookie(BusinessConstant.COOKIE_TEMP_HISTORY_NAME,
                                tempHistoryCookieValue,
                                BusinessConstant.COOKIE_TEMP_HISTORY_EXPIRE);
                        cookieTempHistory = tempHistoryCookieValue;
                    }

                    String key = BusinessConstant.REDIS_TEMP_HISTORY_KEY_PREFIX + cookieTempHistory;
                    //先从Redis中删除重复的该商品的访问记录
                    RedisUtils.lrem(key, BusinessConstant.REDIS_LIST_REMOVE_ALL, String.valueOf(pid));

                    //将这一次的访问记录写入Redis中
                    RedisUtils.lpush(key, String.valueOf(pid));

                }
                //(2)用户登录以后的浏览
                //在已经登录的请况下，不需要通过Cookie中的表示来区分每个客户端
                //在登录的情况下，只需要在Redis中使用userId来区分每个用户的访问记录即可
                //将未登录时候的访问数据和登录时候的访问数据合并
                else {
                    Integer userId = user.getId();
                    String key = BusinessConstant.REDIS_USER_HISTORY_KEY_PREFIX + userId;
                    //先从Redis中删除重复的该商品的访问记录
                    RedisUtils.lrem(key, BusinessConstant.REDIS_LIST_REMOVE_ALL, String.valueOf(pid));

                    //将这一次的访问记录写入Redis中
                    RedisUtils.lpush(key, String.valueOf(pid));
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return product;
    }

    @Override
    public Page<Product> getProductByCondition(ProductQueryParam param) {
        Integer cid = param.getCid();
        String q = param.getQ();
        Integer pageNo = param.getPageNo();
        int defaultPageSize = BusinessConstant.PRODUCT_LIST_DEFAULT_SHOW_COUNT;
        //如果前端没有传递页码，则默认为第1页
        if (null == pageNo) {
            pageNo = BusinessConstant.PRODUCT_LIST_DEFAULT_PAGE_NO;
        }
        Page<Product> page = new Page<>();
        page.setPageNo(pageNo);
        page.setPageSize(defaultPageSize);
        try {

            //1.获取符合条件的总数量
            Long totalCount = productDao.getProductsCountByConditions(param);
            page.setTotalCount(totalCount);
            //2.通过总记录数来计算总页数
            long totalCountModPageSize = totalCount % defaultPageSize;
            long pageCount;
            if (totalCountModPageSize == 0) {
                pageCount = totalCount / defaultPageSize;
            } else {
                pageCount = totalCount / defaultPageSize + 1;
            }
            page.setPageCount(pageCount);

            param.setStart((pageNo - 1) * defaultPageSize);
            page.setData(productDao.getProductsByConditions(param));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return page;

    }
}
