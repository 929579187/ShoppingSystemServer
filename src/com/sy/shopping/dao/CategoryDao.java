package com.sy.shopping.dao;

import com.sy.shopping.entity.Category;

import java.sql.SQLException;
import java.util.List;

public interface CategoryDao {
    /**
     * 根据父级节点的id查找节点信息
     *
     * @param parentId 父节点id
     * @return 父节点对应的子节点信息
     */
    List<Category> getCategoriesByParentId(Integer parentId) throws SQLException;
}
