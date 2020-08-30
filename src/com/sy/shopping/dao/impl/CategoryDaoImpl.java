package com.sy.shopping.dao.impl;

import com.sy.shopping.dao.CategoryDao;
import com.sy.shopping.entity.Category;
import com.sy.shopping.utils.DBUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CategoryDaoImpl implements CategoryDao {
    private QueryRunner runner = new QueryRunner();

    @Override
    public List<Category> getCategoriesByParentId(Integer parentId) throws SQLException {
        try (Connection con = DBUtils.getConnection()) {
            return runner.query(con, "select * from category where parentid=?", new BeanListHandler<>(Category.class), parentId);
        }
    }
}
