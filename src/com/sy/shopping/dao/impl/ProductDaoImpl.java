package com.sy.shopping.dao.impl;

import com.sy.shopping.constant.AppConstant;
import com.sy.shopping.constant.BusinessConstant;
import com.sy.shopping.dao.ProductDao;
import com.sy.shopping.dto.ProductClickCountInfo;
import com.sy.shopping.entity.Product;
import com.sy.shopping.utils.DBUtils;
import com.sy.shopping.vo.ProductQueryParam;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang.StringUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl implements ProductDao {
    private QueryRunner runner = new QueryRunner();

    @Override
    public List<Product> getNewestProducts() throws SQLException {
        try (Connection con = DBUtils.getConnection()) {
            return runner.query(con, "select * from product  order by created desc limit 0,?", new BeanListHandler<>(Product.class), BusinessConstant.NEWEST_PRODUCTS_SHOW_COUNT);
        }
    }

    @Override
    public List<ProductClickCountInfo> getAllClickCountInfoList() throws SQLException {
        try (Connection con = DBUtils.getConnection()) {
            return runner.query(con, "select id,clickcount from product", new BeanListHandler<>(ProductClickCountInfo.class));
        }
    }

    @Override
    public Product getProductById(Integer pid) throws SQLException {
        try (Connection con = DBUtils.getConnection()) {
            return runner.query(con, "select * from product where id=?", new BeanHandler<>(Product.class), pid);
        }
    }

    @Override
    public boolean updateClickCount(int pid, int clickCount) throws SQLException {
        try (Connection con = DBUtils.getConnection()) {
            return runner.update(con, "update product set clickcount=? where id=?", clickCount, pid) > 0;
        }
    }

    @Override
    public List<Product> getProductsByConditions(ProductQueryParam param) throws SQLException {
        try (Connection con = DBUtils.getConnection()) {
            Integer cid = param.getCid();
            String q = param.getQ();
            Integer start = param.getStart();
            StringBuilder sb = new StringBuilder("select * from product where ");
            List<Object> params = new ArrayList<>();
            if (null != cid) {
                sb.append("cid=? ");
                params.add(cid);
            }
            if (StringUtils.isNotBlank(q)) {
                sb.append("title like concat('%',?,'%') or sellpoint like concat('%',?,'%') ");
                params.add(q);
                params.add(q);
            }
            sb.append("limit ?,?");
            params.add(start);
            params.add(BusinessConstant.PRODUCT_LIST_DEFAULT_SHOW_COUNT);
            System.out.println("sql:" + sb.toString());
            return runner.query(con, sb.toString(), new BeanListHandler<>(Product.class),
                    params.toArray());
        }
    }

    @Override
    public Long getProductsCountByConditions(ProductQueryParam param) throws SQLException {
        try (Connection con = DBUtils.getConnection()) {
            Integer cid = param.getCid();
            String q = param.getQ();
            StringBuilder sb = new StringBuilder("select count(*) from product where ");
            List<Object> params = new ArrayList<>();
            if (null != cid) {
                sb.append("cid=? ");
                params.add(cid);
            }
            if (StringUtils.isNotBlank(q)) {
                sb.append("title like concat('%',?,'%') or sellpoint like concat('%',?,'%') ");
                params.add(q);
                params.add(q);
            }
            System.out.println("sql:" + sb.toString());
            return runner.query(con, sb.toString(), new ScalarHandler<>(),
                    params.toArray());
        }
    }
}
