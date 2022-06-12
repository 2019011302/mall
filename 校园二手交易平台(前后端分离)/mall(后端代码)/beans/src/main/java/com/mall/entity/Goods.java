package com.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Goods {
    @TableId(value="goods_id",type = IdType.AUTO)
    private Integer goodsId;
    private String goodsName;
    private String goodsIntroduce;
    private double goodsPrice;
    private String goodsOld;
    private String goodsBargain;
    private Integer goodsType;
    private String goodsSize;
    private String goodsImg;
    private Integer goodsStock;
    private Integer goodsSales;
    private Integer  goodsBuyers;
    private double goodsScore;
    private double goodsPraiserate;
    private Integer goodsStatus;//0.待审核 ；1.在售； 2.首页推荐；-1.下架
    private String goodsWay;
    private Integer sellerId;
    private String sellerType;
}
