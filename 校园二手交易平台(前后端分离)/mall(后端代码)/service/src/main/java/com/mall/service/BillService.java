package com.mall.service;

import com.mall.entity.Bill;
import com.mall.vo.ResultVo;

import java.util.List;

public interface BillService {

    //添加账单
    public ResultVo addBills(Bill bill);

    //查询用户账单
    public ResultVo getBillByUserId (int userId , String userType);
}
