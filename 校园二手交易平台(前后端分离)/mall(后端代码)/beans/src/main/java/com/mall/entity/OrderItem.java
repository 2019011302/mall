package com.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItem {
    @TableId(value="orderItem_id", type = IdType.AUTO)
    private Integer orderItemId;
    private Integer goodsNumber;//商品数量
    private double orderItemMoney;//实际付款
    private double orderItemSum;
    private String orderItemWay;
//    private String orderItemAddress;
    private Date orderItemStime;
    private Date orderItemEtime;
    private String orderItemStatus;
    private Integer orderItemIsComment;
  //  private double orderItemRewardpoints;
    private Integer goodsId;//商品id
    private int orderId;

    //商品图片、商品名称
    private String goodsImg;
    private String goodsName;

    //收货人、收货地址、联系方式
    private String orderItemAddress;//送货地址
    private String orderItemPhone;//收货号码
    private String orderItemName;//收货人姓名
}
