package com.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("bills")
public class Bill {
    @TableId(value="bill_id", type = IdType.AUTO)
    private Integer billId;
    private Date billTime;
    private double billMoney;
    private double billAccount;
    @TableField("user_id")
    private Integer userId;
    private String userType;
    private String billOther;
}
