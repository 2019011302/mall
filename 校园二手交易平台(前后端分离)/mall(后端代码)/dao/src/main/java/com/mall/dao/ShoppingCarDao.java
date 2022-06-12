package com.mall.dao;

import com.mall.entity.ShoppingCar;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCarDao {
    //通过用户id获取购物车信息
    public List<ShoppingCar> getShoppingCarByUserId(int UserId);

    //添加购物车
    public int addShoppingCar(ShoppingCar shoppingCar);

    //修改购物车
    public int updateShoppingCar(ShoppingCar shoppingCar);

    //删除购物车
    public int deleteShoppingCar(int carId);

    //通过购物车id查询购物车
    public ShoppingCar getShoppingCarByCarId(int carId);


}
