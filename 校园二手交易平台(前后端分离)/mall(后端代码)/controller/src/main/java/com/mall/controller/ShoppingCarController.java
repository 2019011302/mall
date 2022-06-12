package com.mall.controller;

import com.mall.entity.ShoppingCar;
import com.mall.service.ShoppingCarService;
import com.mall.vo.ResultVo;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/shopcar")
@CrossOrigin
@Slf4j
public class ShoppingCarController {

    @Autowired
    ShoppingCarService shoppingCarService;

    //获得某用户的购物车信息
    @GetMapping("/list")
    public ResultVo listCars(@RequestParam("userId") int userId){

        return shoppingCarService.getShoppingCarByUserId(userId);
    }
    //添加
    @PostMapping("/add")
    public ResultVo addShoppingCar(@RequestBody ShoppingCar shoppingCar){
        return shoppingCarService.addShoppingCar(shoppingCar);
    }

    //通过购物车id查询购物车，返回一串购物车数据
    @GetMapping("/carIdlist")
    public ResultVo getShoppingCarByCarId(String carIdlist){
//        System.out.print(carIdlist);
        return shoppingCarService.getShoppingCarByCarId(carIdlist);
    }

    //删除购物车
    @DeleteMapping("/delete")
    public ResultVo deleteCarByCarId(int carId){
        return shoppingCarService.deleteCarByCarId(carId);
    }
}

