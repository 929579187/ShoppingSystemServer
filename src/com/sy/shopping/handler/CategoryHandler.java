package com.sy.shopping.handler;

import com.sy.shopping.annotations.RequestMapping;
import com.sy.shopping.dto.CategoryTreeNode;
import com.sy.shopping.service.CategoryService;
import com.sy.shopping.service.impl.CategoryServiceImpl;

import java.util.List;

@RequestMapping("category")
public class CategoryHandler {
    private CategoryService categoryService = new CategoryServiceImpl();

    @RequestMapping("all")
    public List<CategoryTreeNode> getCategoryTree() {
        return categoryService.getCategoryTree();
    }

}
