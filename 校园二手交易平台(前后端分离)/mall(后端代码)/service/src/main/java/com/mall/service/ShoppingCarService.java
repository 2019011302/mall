package com.mall.service;

import com.mall.entity.ShoppingCar;
import com.mall.vo.ResultVo;

import java.util.List;

public interface ShoppingCarService {

    //通过用户id获取购物车信息
    public ResultVo getShoppingCarByUserId(int userId);

    //添加购物车
    public ResultVo addShoppingCar(ShoppingCar shoppingCar);

    //通过购物车id列表查询购物车
     public ResultVo getShoppingCarByCarId(String carId);

     //删除购物车
    public ResultVo deleteCarByCarId(int carId);

}
