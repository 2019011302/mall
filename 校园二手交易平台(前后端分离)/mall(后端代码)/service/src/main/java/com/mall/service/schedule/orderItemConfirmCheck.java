package com.mall.service.schedule;

import com.mall.dao.OrderItemDao;
import com.mall.entity.OrderItem;
import com.mall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class orderItemConfirmCheck {

    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private OrderService orderService;

    @Scheduled(cron="0/5 * * * * ?")
    public void checkAndConfirmOrderItem(){
        //时间
        Date time = new Date(System.currentTimeMillis()-7*24*60*60*1000);
        //搜索满足条件的订单项 item.etime < time
        List<OrderItem> list = orderItemDao.getOutTimeOrderItem(time);
        //修改订单项的状态为“交易完成"
        for(int i = 0 ; i < list.size() ; i++){
            OrderItem orderItem = list.get(i);
            //收货
            orderService.takeDelivery(orderItem.getOrderItemId());
            //orderItem.setOrderItemStatus("交易完成");
            //orderItemDao.updateOrderItem(orderItem);
        }
    }
}
