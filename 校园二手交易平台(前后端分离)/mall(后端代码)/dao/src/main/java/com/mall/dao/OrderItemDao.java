package com.mall.dao;

import com.mall.entity.OrderItem;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderItemDao {

    //添加订单项
    public int addOrderItem( OrderItem orderItem);

    //根据用户id 查找订单项
    public List<OrderItem> getOrderItemByUserId(int userId);

    //根据商家id查找订单项
    public List<OrderItem> getOrderItemBySellerId(int sellerId);

    //修改订单项
    public int updateOrderItem(OrderItem orderItem);

    //根据订单号查找订单项
    public OrderItem getOrderItemById(int orderItemId);

    //查找已经收货，但尚未确认收货，且时间超过7天的订单 , 参数为标志日期 ， 若收货时间小于标志时间，说明已经说明收货时间满七天
    public List<OrderItem> getOutTimeOrderItem(Date time);

}
