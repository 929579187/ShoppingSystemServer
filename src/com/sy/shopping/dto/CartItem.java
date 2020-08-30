package com.sy.shopping.dto;

import java.math.BigDecimal;

/**
 * 用于表示购物车中的每个商品的信息
 */
public class CartItem {
    private Integer pid;
    private String productName;
    private BigDecimal price;
    private Integer count;
    private String image;
    public Integer getPid() {
        return pid;
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getCount() {
        return count;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
