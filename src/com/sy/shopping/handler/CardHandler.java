package com.sy.shopping.handler;

import com.sy.shopping.annotations.RequestMapping;
import com.sy.shopping.dto.CartItem;
import com.sy.shopping.service.CartService;
import com.sy.shopping.service.impl.CartServiceImpl;
import com.sy.shopping.utils.HttpUtils;

import java.util.List;

@RequestMapping("cart")
public class CardHandler {
    private CartService cartService = new CartServiceImpl();

    /**
     * 向购物车中添加一条数据
     */
    @RequestMapping("add")
    public void addCart() {
        Integer pid = HttpUtils.getParameter("pid", Integer.class, "商品id格式不合法");
        Integer count = HttpUtils.getParameter("count", Integer.class, "数量不合法");
        cartService.addCart(pid, count);
    }

    /**
     * 获取购物车中的具体数据
     */
    @RequestMapping("get")
    public List<CartItem> getCart() {
        return cartService.getCart();
    }

    /**
     * 改变购物车中某个商品的数量
     */
    @RequestMapping("changeCount")
    public void changeCount() {
        Integer pid = HttpUtils.getParameter("pid", Integer.class, "商品id格式不合法");
        Integer count = HttpUtils.getParameter("count", Integer.class, "数量不合法");
        cartService.changeCartItemCount(pid, count);
    }
}
