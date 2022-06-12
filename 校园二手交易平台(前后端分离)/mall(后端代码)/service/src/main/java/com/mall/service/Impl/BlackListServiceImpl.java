package com.mall.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.dao.BlackListDao;
import com.mall.dao.UserDao;
import com.mall.entity.Black;
import com.mall.entity.User;
import com.mall.service.BlackListService;
import com.mall.vo.ResStatus;
import com.mall.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BlackListServiceImpl implements BlackListService {
    @Autowired
    BlackListDao blackListDao;
    @Autowired
    UserDao userDao;

    @Override
    public ResultVo addBlackList(int orderItemId , int sellerId , String sellerType) {
        User user = userDao.getUserByOrderItemId(orderItemId);
        Black black = new Black();
        black.setUserId(user.getUserId());
        black.setSellerId(sellerId);
        black.setSellerType(sellerType);
        black.setTime(new Date());
        int i = blackListDao.insert(black);
        if(i>0){
            return new ResultVo(ResStatus.OK , "success",null);
        }
        return new ResultVo(ResStatus.NO , "fail",null);
    }

    @Override
    public ResultVo selectBlackList(int sellerId, String sellerType) {
        QueryWrapper<Black> wrapper = new QueryWrapper<>();
        wrapper.eq("seller_id" , sellerId)
                .eq("seller_type" ,sellerType );
        List<Black> list = blackListDao.selectList(wrapper);
        if(list != null){
            return new ResultVo(ResStatus.OK , "success",list);
        }
        return new ResultVo(ResStatus.NO , "fail",null);
    }

    @Override
    public ResultVo deleteBlackList(int blackId) {
        int i = blackListDao.deleteById(blackId);
        if(i>0){
            return new ResultVo(ResStatus.OK , "success",null);
        }
        return new ResultVo(ResStatus.NO , "fail",null);
    }
}
