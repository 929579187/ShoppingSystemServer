package com.sy.shopping.service.impl;

import com.sy.shopping.constant.BusinessConstant;
import com.sy.shopping.dao.ProductDao;
import com.sy.shopping.dao.impl.ProductDaoImpl;
import com.sy.shopping.dto.IndexPageData;
import com.sy.shopping.entity.Product;
import com.sy.shopping.service.IndexService;
import com.sy.shopping.utils.RedisUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IndexServiceImpl implements IndexService {
    private ProductDao productDao = new ProductDaoImpl();

    @Override
    public IndexPageData getIndexPageData() {
        IndexPageData indexPageData = new IndexPageData();
        try {
            //获取最新商品信息
            indexPageData.setNewestProducts(productDao.getNewestProducts());
            //获取最热商品信息
            //1.从Redis中获取商品点击量的信息
            Map<String, String> redisClickCountMap = RedisUtils.hgetall(BusinessConstant.REDIS_CLICK_COUNT_KEY);
            //2.对点击量进行降序排列，取前10点击量对应的商品id
            Map<String, Double> members = new LinkedHashMap<>();
            redisClickCountMap.entrySet().stream().forEach(ele -> members.put(ele.getKey(), Double.parseDouble(ele.getValue())));
            RedisUtils.zadd(BusinessConstant.REDIS_CLICK_COUNT_SORTED_SET_KEY, members);
            Set<String> topNMembers = RedisUtils.zrevrange(BusinessConstant.REDIS_CLICK_COUNT_SORTED_SET_KEY,
                    BusinessConstant.REDIS_CLICK_COUNT_START_OFFSET,
                    BusinessConstant.HOTEST_PRODUCTS_SHOW_COUNT - 1);
            //3.到MySQL中查看这些id对应的商品信息
            List<Product> hotestProducts = new ArrayList<>();
            for (String topNMember : topNMembers) {
                hotestProducts.add(productDao.getProductById(Integer.parseInt(topNMember)));
            }
            indexPageData.setHotestProducts(hotestProducts);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return indexPageData;
    }
}
