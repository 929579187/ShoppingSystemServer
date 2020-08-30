package com.sy.shopping.vo;

/**
 * 用于搜索商品信息的条件对象
 */
public class ProductQueryParam {
    /**
     * 分类id
     */
    private Integer cid;

    /**
     * 关键字
     */
    private String q;

    /**
     * 分页查询中的页码
     * 如果前端没有传递页码，则默认查询第1页的数据
     */
    private Integer pageNo;
    /**
     * limit语法中的起始索引
     */
    private Integer start;

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }


    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }
}
