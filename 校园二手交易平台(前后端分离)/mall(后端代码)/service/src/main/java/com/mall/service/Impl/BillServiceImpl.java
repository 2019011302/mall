package com.mall.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mall.dao.BillDao;
import com.mall.dao.UserDao;
import com.mall.entity.Bill;
import com.mall.entity.User;
import com.mall.service.BillService;
import com.mall.vo.ResStatus;
import com.mall.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class BillServiceImpl implements BillService {
    @Autowired
    BillDao billDao;
    @Autowired
    UserDao userDao;
    @Override
    public ResultVo addBills(Bill bill) {
        int i = billDao.insert(bill);
        if(i > 0){
            //修改用户的账户余额
            User user = userDao.selectById(bill.getUserId());
//                    getUserByUserId(bill.getUserId());
            user.setUserAccount(bill.getBillAccount());
            int j =userDao.updateById(user);
            if(j>0){
                return new ResultVo(ResStatus.OK , "success",j);
            }
            return new ResultVo(ResStatus.NO , "fail",null);
        }else{
            return new ResultVo(ResStatus.NO , "fail",null);
        }
    }

    @Override
    public ResultVo getBillByUserId(int userId , String userType) {
        QueryWrapper<Bill> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId)
                .eq("user_type" , userType)
                 .orderByDesc("bill_id");
        List<Bill> list = billDao.selectList(wrapper);
        if(list != null){
            return new ResultVo(ResStatus.OK , "success",list);
        }else{
            return new ResultVo(ResStatus.NO , "fail",null);
        }
    }
}
