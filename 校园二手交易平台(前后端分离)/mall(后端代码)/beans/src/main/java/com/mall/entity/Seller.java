package com.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mall.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("sellers")
public class Seller extends BaseEntity {
    @TableId(value="seller_id", type = IdType.AUTO)
    private Integer sellerId;
    private Integer sellerScale;
    private String sellerIdentityCard;
    private String sellerPaper;
}
