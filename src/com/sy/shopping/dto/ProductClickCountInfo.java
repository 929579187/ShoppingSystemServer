package com.sy.shopping.dto;

/**
 * 商品的点击量信息
 */
public class ProductClickCountInfo {

    /**
     * 商品id
     */
    private Integer id;

    /**
     * 点击量
     */
    private Integer clickCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClickCount() {
        return clickCount;
    }

    public void setClickCount(Integer clickCount) {
        this.clickCount = clickCount;
    }

    @Override
    public String toString() {
        return "ProductClickCountInfo{" +
                "id=" + id +
                ", clickCount=" + clickCount +
                '}';
    }
}
