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
@TableName("cars")
public class ShoppingCar {
    @TableId(value="car_id", type = IdType.AUTO)
    private Integer carId;
    private Integer carNumber;
    private Date carTime;
    private Integer userId;
    private Integer goodsId;

    //商品名、商品图片、商品价格、库存、状态(是否下架)
    private String goodsName;
    private String goodsImg;
    private Double goodsPrice;
    private Integer goodsStock;
    private String goodsWay;
    private Integer goodsStatus;
}
