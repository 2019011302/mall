package com.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("orders")
public class Order {
    //订单属性
    @TableId(value="order_id",type = IdType.AUTO)
    private Integer orderId;//订单id
    private Integer userId;//用户id
    private Date orderTime;//下单时间
    private double orderSum;//总额
    private double orderMoney;//实际付款
    private String orderAddress;//送货地址
    private String orderPhone;//收货号码
    private String orderName;//收货人姓名
}
