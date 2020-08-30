package com.sy.shopping.service;

import com.sy.shopping.dto.CategoryTreeNode;

import java.util.List;

public interface CategoryService {
    /**
     * 获取分类树信息
     *
     * @return 分类树
     */
    List<CategoryTreeNode> getCategoryTree();
}
