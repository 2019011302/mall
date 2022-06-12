package com.mall.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends BaseMapper<User> {

    //根据订单项id查找买家
    public User getUserByOrderItemId(int orderItemId);
    //根据订单项id查找卖家
    public User getSellerByOrderItemId(int orderItemId);
}
