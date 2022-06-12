package com.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@TableName("refunds")
public class Refund {
    @TableId(value="refund_id", type = IdType.AUTO)
    private Integer refundId;
    private String refundReason;
    private String refundResult;
    private Date refundTime;
    private String refundOther;
    @TableField("orderItem_id")
    private Integer orderItemId;

}
