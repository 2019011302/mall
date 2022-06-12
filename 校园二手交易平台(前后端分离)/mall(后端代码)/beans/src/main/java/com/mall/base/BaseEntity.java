package com.mall.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public abstract class BaseEntity {

    private String userName;
    private String userSex;
    private String userPassword;
    private String userPhone;
    private String userEmail;
    private String userCity;
    private String userCard;
    private Integer userStatus;
    private String userImg;
    private String userWecat;
    private double userAccount;
    private double userScore;
    private double userSale;
    private Integer userComments;
}
