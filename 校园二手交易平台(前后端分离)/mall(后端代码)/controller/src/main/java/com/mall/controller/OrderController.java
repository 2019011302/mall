package com.mall.controller;

import com.mall.entity.OrderItem;
import com.mall.entity.ShoppingCar;
import com.mall.service.OrderService;
import com.mall.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

@RestController
@RequestMapping("/order")
@CrossOrigin
@Slf4j
public class OrderController {
    @Autowired
    OrderService orderService;

    //从购物车下单
    @PostMapping("/add")
    public ResultVo addOrder(int userId, double sumMoney , double realMoney , String carIdList){
        ResultVo vo = orderService.addOrder(userId , sumMoney, realMoney , carIdList );
        return vo;
    }
    //从商品详情页下单
    @PostMapping("/addOne")
    public ResultVo addOneOrder(int userId , int goodsId , int goodsNumber , double sumMoney ,  double realMoney ) {
        return orderService.addOneOrder(userId , goodsId , goodsNumber, sumMoney , realMoney);
    }
    //用户订单
    @GetMapping("/userlist")
    public ResultVo getOrderItemListByUserId(@RequestParam("userId") int userId){
        return orderService.getOrderItemByUserId(userId);
    }

    //商家订单
    @GetMapping("/sellerlist")
    public ResultVo getOrderItemListBySellerId(@RequestParam("sellerId") int sellerId){
        return orderService.getOrderItemBySellerId(sellerId);
    }

    //更改订单项
    @PutMapping("/update")
    public ResultVo updateOrderItem(@RequestBody  OrderItem orderItem){
        return orderService.updateOrderItem(orderItem);
    }
    //通过订单项id查找订单项 ,用于退货时的信息确认
    @GetMapping("/find")
    public ResultVo getOrderItemById(int orderItemId) {
        return orderService.getOrderItemById(orderItemId);
    }

    //订单项确认收货
    @PostMapping("/takeDelivery")
    public ResultVo takeDelivery(int orderItemId ){
        return orderService.takeDelivery(orderItemId);
    }
}
