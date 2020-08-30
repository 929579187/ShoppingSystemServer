package com.sy.shopping.service.impl;

import com.sy.shopping.constant.BusinessConstant;
import com.sy.shopping.dao.ProductDao;
import com.sy.shopping.dao.impl.ProductDaoImpl;
import com.sy.shopping.dto.CartItem;
import com.sy.shopping.entity.Product;
import com.sy.shopping.entity.User;
import com.sy.shopping.exception.BusinessException;
import com.sy.shopping.service.CartService;
import com.sy.shopping.service.UserService;
import com.sy.shopping.utils.RedisUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CartServiceImpl implements CartService {
    private ProductDao productDao = new ProductDaoImpl();
    private UserService userService = new UserServiceImpl();

    @Override
    public void addCart(Integer pid, Integer count) {
        //1.验证pid对应的商品是否存在
        Product product;
        try {
            product = productDao.getProductById(pid);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        if (null == product) {
            throw new BusinessException("该商品不存在");
        }
        //2.验证count对应的数量是否合法
        if (count <= 0) {
            throw new BusinessException("加入购物车的商品数量必须为正数");
        }
        //获取用户的id
        User user = userService.getLoginUser();
        Integer userId = user.getId();

        //Redis中当前用户对应的购物车信息的Key的值
        String key = BusinessConstant.REDIS_CART_KEY_PREFIX + userId;
        RedisUtils.hincrby(key, String.valueOf(pid), Long.parseLong(String.valueOf(count)));

    }

    @Override
    public List<CartItem> getCart() {
        //1.获取当前登录用户的购物车数据
        Integer userId = userService.getLoginUser().getId();
        String key = BusinessConstant.REDIS_CART_KEY_PREFIX + userId;
        Map<String, String> redisCartMap = RedisUtils.hgetall(key);
        //2.构建 List<CartItem>对象
        List<CartItem> cartItems = new ArrayList<>();
        for (Map.Entry<String, String> redisCartItem : redisCartMap.entrySet()) {
            Integer pid = Integer.parseInt(redisCartItem.getKey());
            Integer count = Integer.parseInt(redisCartItem.getValue());
            CartItem cartItem = new CartItem();
            cartItem.setPid(pid);
            cartItem.setCount(count);
            Product product;
            try {
                product = productDao.getProductById(pid);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            cartItem.setPrice(product.getPrice());
            cartItem.setProductName(product.getTitle());
            cartItem.setImage(product.getImage());
            cartItems.add(cartItem);
        }
        return cartItems;
    }

    @Override
    public void changeCartItemCount(Integer pid, Integer count) {
        RedisUtils.hset(
                BusinessConstant.REDIS_CART_KEY_PREFIX + userService.getLoginUser().getId(),
                String.valueOf(pid),
                String.valueOf(count));
    }

//    public static void main(String[] args) {
//        Connection con = null;
//        StateMent stmt = null;
//        ResultSet rs = null;
//        try{
//            Class.forName("com.mysql.jdbc.Driver");
//            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db2?useSSL=false","root","root")
//            stmt = con.creatStateMent();
//            rs = stmt.executeQuery("");
//            while(rs.next()){
//                System.out.println("");
//            }
//
//        }catch(ClassNotFoundException e){
//            System.out.println("驱动类加载失败");
//        }catch(SQLException e){
//            e.printStackTrace;
//        }finally{
//            if(null!=rs){
//                try{
//                    rs.close;
//                } catch(SQLException e){}
//            }
//            if(null!=stmt){
//                try{stmt.close;}catch(SQLException e){}
//            }
//            if(null!=con){
//                try{con.close;}catch(SQLException e){}
//            }
//
//        }
//    }
}
