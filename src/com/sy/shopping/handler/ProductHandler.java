package com.sy.shopping.handler;

import com.sy.shopping.annotations.RequestMapping;
import com.sy.shopping.dto.Page;
import com.sy.shopping.entity.Product;
import com.sy.shopping.service.ProductService;
import com.sy.shopping.service.impl.ProductServiceImpl;
import com.sy.shopping.utils.HttpUtils;
import com.sy.shopping.vo.ProductQueryParam;

@RequestMapping("product")
public class ProductHandler {
    private ProductService productService = new ProductServiceImpl();

    @RequestMapping("getById")
    public Product getProductById() {
        //获取请求参数中的pid
        return productService.getProductById(
                HttpUtils.getParameter("pid", Integer.class
                        , "商品id格式不合法"));
    }

    @RequestMapping("conditions")
    public Page<Product> getProductsByConditions() {
        Integer cid = HttpUtils.getParameter("cid", Integer.class, "分类编号格式不合法");
        String q = HttpUtils.getParameter("q", String.class);
        Integer pageNo = HttpUtils.getParameter("pageNo", Integer.class, "页码格式不合法");
        ProductQueryParam param = new ProductQueryParam();
        param.setPageNo(pageNo);
        param.setQ(q);
        param.setCid(cid);
        return productService.getProductByCondition(param);
    }
}
