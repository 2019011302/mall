package com.mall.service;

import com.mall.entity.Refund;
import com.mall.vo.ResultVo;

public interface RefundService {
    //添加
    public ResultVo addRefund(Refund refund);

    //根据订单项id获得退货信息
    public ResultVo getOneRefund(int orderItemId);

    //卖家处理退货信息
    public ResultVo checkRefund(int refundId , String other , Boolean bool);

}
