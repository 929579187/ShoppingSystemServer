package com.sy.shopping.dto;

import java.util.List;

/**
 * 分类信息节点对象
 */
public class CategoryTreeNode {
    private Integer id;
    private String name;
    /**
     * 子节点的信息
     */
    private List<CategoryTreeNode> childNodes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CategoryTreeNode> getChildNodes() {
        return childNodes;
    }

    public void setChildNodes(List<CategoryTreeNode> childNodes) {
        this.childNodes = childNodes;
    }

    @Override
    public String toString() {
        return "CategoryTreeNode{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", childNodes=" + childNodes +
                '}';
    }
}
