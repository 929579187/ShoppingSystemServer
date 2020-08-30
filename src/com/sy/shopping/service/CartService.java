package com.sy.shopping.service;

import com.sy.shopping.dto.CartItem;

import java.util.List;

public interface CartService {
    /**
     * 加入购物车
     *
     * @param pid   商品id
     * @param count 要加入购物车的商品数量
     */
    void addCart(Integer pid, Integer count);

    /**
     * 获取当前登录用户的购物车数据
     *
     * @return 购物车数据
     */
    List<CartItem> getCart();

    /**
     * 修改购物车中某个商品的数量
     *
     * @param count 数量
     * @param pid   商品id
     */
    void changeCartItemCount(Integer pid, Integer count);
}
