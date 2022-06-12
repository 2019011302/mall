package com.mall.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.entity.Seller;
import com.mall.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerDao extends BaseMapper<Seller> {
    //根据订单项id查找商家
    public Seller getSellerByOrderItemId(int orderItemId);
}
