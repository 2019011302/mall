package com.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall.base.BaseEntity;
import lombok.*;
import com.baomidou.mybatisplus.annotation.TableName;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("users")
public class User extends BaseEntity {
    @TableId(value="user_id" , type = IdType.AUTO)
    private Integer userId;
    private String userIntroduce;
    private String userAddress ;
    private double userRewardpoints;
}
