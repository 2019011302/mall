package com.mall.service;

import com.mall.entity.OrderItem;
import com.mall.vo.ResultVo;

import java.util.Date;
import java.util.List;

public interface OrderService {

    //添加订单，从购物车加购
    public ResultVo addOrder(int userId , double sumMoney , double realMoney , String carIdList);

    //添加订单项(从商品详情页添加）
    public ResultVo addOneOrder(int userId , int goodsId , int goodsNumber , double sumMoney ,  double realMoney );

    //跟据用户名获得订单项
    public ResultVo getOrderItemByUserId(int userId);

    //根据商家号查找订单项
    public ResultVo getOrderItemBySellerId(int userId);

    //修改订单项
    public ResultVo updateOrderItem(OrderItem orderItem);

    //根据订单号查找订单项
    public ResultVo getOrderItemById(int orderItemId);

    //收货
    public ResultVo takeDelivery(int orderItemId);
}
