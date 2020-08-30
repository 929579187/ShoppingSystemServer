package com.sy.shopping.service;

import com.sy.shopping.dto.Page;
import com.sy.shopping.entity.Product;
import com.sy.shopping.vo.ProductQueryParam;

public interface ProductService {

    /**
     * 用于将MySQL数据库中的商品点击量同步到Redis中
     *
     * @return 是否同步成功
     */
    boolean syncProductsClickCountToRedis();


    /**
     * 用于将Redis中的点击量同步到MySQL中
     *
     * @return 是否同步成功
     */
    boolean syncProductsClickCountToMySQL();

    /**
     * 根据商品id获取商品信息
     *
     * @param pid 商品id
     * @return 指定id的商品信息
     */
    Product getProductById(Integer pid);


    /**
     * 根据条件分页搜索产品信息
     *
     * @param param 搜索条件
     * @return 搜索后的分页结果
     */
    Page<Product> getProductByCondition(ProductQueryParam param);
}
