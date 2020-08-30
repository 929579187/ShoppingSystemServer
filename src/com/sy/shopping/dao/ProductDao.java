package com.sy.shopping.dao;

import com.sy.shopping.dto.ProductClickCountInfo;
import com.sy.shopping.entity.Product;
import com.sy.shopping.vo.ProductQueryParam;

import java.sql.SQLException;
import java.util.List;

public interface ProductDao {

    /**
     * 用于获取最新商品信息
     *
     * @return 最新商品信息
     */
    List<Product> getNewestProducts() throws SQLException;

    /**
     * 获取所有商品的点击量数据
     *
     * @return 所有商品的点击量数据
     */
    List<ProductClickCountInfo> getAllClickCountInfoList() throws SQLException;


    /**
     * 根据id后去商品信息
     *
     * @param pid 商品id
     * @return 指定id的商品信息
     */
    Product getProductById(Integer pid) throws SQLException;


    /**
     * 更新点击量的操作
     *
     * @param pid        商品id
     * @param clickCount 要更新进去的点击量
     * @return 是否更新成功
     */
    boolean updateClickCount(int pid, int clickCount) throws SQLException;

    /**
     * 根据条件来获取商品信息
     *
     * @param param 搜索商品信息的搜索条件
     * @return 符合搜索条件的商品信息
     */
    List<Product> getProductsByConditions(ProductQueryParam param) throws SQLException;

    /**
     * 根据条件来获取商品的数量
     *
     * @param param 搜索商品信息的搜索条件
     * @return 符合搜索条件的商品总数量
     */
    Long getProductsCountByConditions(ProductQueryParam param) throws SQLException;
}
