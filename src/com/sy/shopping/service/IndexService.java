package com.sy.shopping.service;

import com.sy.shopping.dto.IndexPageData;
import com.sy.shopping.entity.Product;

import java.util.List;
import java.util.Map;

public interface IndexService {
    /**
     * 用来获取首页的要展示的数据
     *
     * @return 返回值中的key表示是什么数据，value表示对应的商品列表
     */
//    Map<String, List<Product>> getIndexPageData();

    /**
     * 用来获取首页的要展示的数据
     *
     * @return 返回用于展示的首页数据
     */
    IndexPageData getIndexPageData();
}
