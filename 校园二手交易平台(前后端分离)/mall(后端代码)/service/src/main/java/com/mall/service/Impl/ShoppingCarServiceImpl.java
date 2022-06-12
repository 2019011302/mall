package com.mall.service.Impl;

import com.mall.dao.ShoppingCarDao;
import com.mall.entity.ShoppingCar;
import com.mall.service.ShoppingCarService;
import com.mall.vo.ResStatus;
import com.mall.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
@Service
@Slf4j
public class ShoppingCarServiceImpl implements ShoppingCarService {
    @Autowired
    ShoppingCarDao shoppingCarDao;
    @Override
    public ResultVo getShoppingCarByUserId(int userId) {
        List<ShoppingCar> list = shoppingCarDao.getShoppingCarByUserId(userId);
        if(list == null){
            return new ResultVo(ResStatus.NO , "fail" , null);
        }
        return new ResultVo(ResStatus.OK , "success" , list);
    }

    @Override
    public ResultVo addShoppingCar(ShoppingCar shoppingCar) {
        //获得用户购物车列表
        List<ShoppingCar> list = shoppingCarDao.getShoppingCarByUserId(shoppingCar.getUserId());
        boolean bool = false;
        int flag = 0;
        String msg="添加成功";
        //查看待添加商品是否在列表中
        for(int i =0 ; i <list.size() ; i++){
            ShoppingCar car = list.get(i);
            if(shoppingCar.getGoodsId() == car.getGoodsId()){
                bool=true;
                shoppingCar.setCarId(car.getCarId());
                //在，则判断 已在购物车的商品和添加数量之和 与 商品库存 的关系
                if(car.getCarNumber() + shoppingCar.getCarNumber() <= car.getGoodsStock()){
                    //若小于等于 则修改购物车数量为 已在购物车的商品和添加数量之和
                    shoppingCar.setCarNumber(car.getCarNumber() + shoppingCar.getCarNumber() );
                }else{
                    //若大于 则 修改购物车数量为商品库存
                    shoppingCar.setCarNumber(car.getGoodsStock());
                    msg = "加购数量达到上限，已按最大数量添加";
                }
                flag = shoppingCarDao.updateShoppingCar(shoppingCar);
                break;
            }
        }
        //不在，则添加商品
        if(!bool){
            flag = shoppingCarDao.addShoppingCar(shoppingCar);
        }

        if(flag>0){
            return new ResultVo(ResStatus.OK , msg , null);
        }
        return new ResultVo(ResStatus.NO , "添加失败" , null);
    }
    //通过购物车id列表查询购物车
    //通过购物车id列表查询购物车
    public ResultVo getShoppingCarByCarId(String carId){

        String[] carIdList = carId.split(",");
        List<ShoppingCar> list = new ArrayList<>();
        for(int i = 0 ; i< carIdList.length ; i++){
            ShoppingCar shoppingCar = shoppingCarDao.getShoppingCarByCarId(Integer.parseInt(carIdList[i]));
            list.add(shoppingCar);
        }
        if(list.size()!=0){
            return new ResultVo(ResStatus.OK , "success" , list);
        }
        return new ResultVo(ResStatus.NO , "fail" , null);
    }

    @Override
    public ResultVo deleteCarByCarId(int carId) {
        int i = shoppingCarDao.deleteShoppingCar(carId);
        if(i > 0){
            return new ResultVo(ResStatus.OK , "success" , i);
        }
        return new ResultVo(ResStatus.NO , "fail" , null);
    }
}
