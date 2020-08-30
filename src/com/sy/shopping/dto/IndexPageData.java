package com.sy.shopping.dto;

import com.sy.shopping.entity.Product;

import java.util.List;

/**
 * 用来表示首页要展示的数据
 */
public class IndexPageData {
    /**
     * 最新商品信息
     */
    private List<Product> newestProducts;
    /**
     * 最热商品信息
     */
    private List<Product> hotestProducts;

    public List<Product> getNewestProducts() {
        return newestProducts;
    }

    public void setNewestProducts(List<Product> newestProducts) {
        this.newestProducts = newestProducts;
    }

    public List<Product> getHotestProducts() {
        return hotestProducts;
    }

    public void setHotestProducts(List<Product> hotestProducts) {
        this.hotestProducts = hotestProducts;
    }

    @Override
    public String toString() {
        return "IndexPageData{" +
                "newestProducts=" + newestProducts +
                ", hotestProducts=" + hotestProducts +
                '}';
    }
}
