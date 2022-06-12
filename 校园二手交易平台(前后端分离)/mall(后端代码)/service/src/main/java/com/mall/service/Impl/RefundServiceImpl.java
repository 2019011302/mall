package com.mall.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mall.dao.*;
import com.mall.entity.*;
import com.mall.service.RefundService;
import com.mall.vo.ResStatus;
import com.mall.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.Date;

@Service
@Slf4j
public class RefundServiceImpl implements RefundService {
    @Autowired
    RefundDao refundDao;
    @Autowired
    OrderItemDao orderItemDao;
    @Autowired
    UserDao userDao;
    @Autowired
    BillDao billDao;
    @Autowired
    SellerDao sellerDao;
    @Autowired
    GoodsDao goodsDao;
    @Override
    public ResultVo addRefund(Refund refund) {
        try {
            OrderItem orderItem = orderItemDao.getOrderItemById(refund.getOrderItemId());
            if (orderItem.getOrderItemStatus().equals("已收货")) {//判断是否收货
                Date now = new Date();
                Date old = orderItem.getOrderItemEtime();
                long between = now.getTime() - old.getTime();
                if (between > 24 * 60 * 60 * 1000) {//判断收货时间是否超过24小时
                    return new ResultVo(ResStatus.NO, "收货时间已经超过24小时，不能退款！", null);
                }
            }
            //否，添加退货记录，修改订单项状态为”退款中“
            refund.setRefundTime(new Date());
            int i = refundDao.insert(refund);
            if (i > 0) {
                orderItem.setOrderItemStatus("退款中");
                orderItemDao.updateOrderItem(orderItem);
                return new ResultVo(ResStatus.OK, "success", null);
            }
        }
        catch (Exception e) {
                e.printStackTrace();
                return new ResultVo(ResStatus.NO, "fail", null);
            }
        return new ResultVo(ResStatus.NO , "fail" , null);
    }


    @Override
    public ResultVo getOneRefund(int orderItemId){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("orderItem_id" , orderItemId);
        Refund refund = refundDao.selectOne(wrapper);
        if(refund !=null){
            return new ResultVo(ResStatus.OK, "success", refund);
        }
        return new ResultVo(ResStatus.NO , "fail" , null);
    }

    @Override
    @Transactional
    public ResultVo checkRefund(int refundId , String other , Boolean bool) {
        //退款成功：
        //1.修改订单项状态为"交易关闭"(更新订单)
        //2.修改退货表的退货信息，修改退款结果和卖家备注(更新退货表)
        //3.将金额和积分退还给用户

        //退款失败
        //1.修改订单项状态为"退款失败"(更新订单)
        //2.修改退货表的退货信息，修改退款结果和卖家备注(更新退货表)
        try{
            Refund refund = refundDao.selectById(refundId);
            refund.setRefundOther(other);
            OrderItem orderItem = orderItemDao.getOrderItemById(refund.getOrderItemId());
            orderItem.setOrderItemStatus(refund.getRefundResult());
            if(bool){
                orderItem.setOrderItemStatus("交易关闭");
                refund.setRefundResult("退款成功");
            }else{
                orderItem.setOrderItemStatus("退款失败");
                refund.setRefundResult("退款失败");
            }
            //1.修改订单项状态
            orderItemDao.updateOrderItem(orderItem);
            //2.修改退货表的退货信息
            refundDao.updateById(refund);
            //3.若退款成功，则退还金额、积分给用户
            //4.添加用户账单
            if(bool){
                User user = userDao.getUserByOrderItemId(refund.getOrderItemId());
                //用户金额
                double money = orderItem.getOrderItemMoney() + user.getUserAccount();
                //用户积分
                double points = orderItem.getOrderItemSum() - orderItem.getOrderItemMoney()+user.getUserRewardpoints();
                //保留两位小数
                DecimalFormat df = new DecimalFormat("#.##");
                double new_money = Double.parseDouble(df.format(money));
                double new_points = Double.parseDouble(df.format(points));
                //修改
                user.setUserRewardpoints(new_points);
                user.setUserAccount(new_money);
                userDao.updateById(user);
                //添加账单 买家的账单。因为金额未到账，所以没有卖家的账单
                //添加买家账单
                Date time = new Date();
                Bill userbill = new Bill();
                userbill.setBillMoney(orderItem.getOrderItemMoney());
                userbill.setUserId(user.getUserId());
                userbill.setBillOther("订单"+orderItem.getOrderItemId()+"退款");
                userbill.setBillTime(time);
                userbill.setBillAccount(user.getUserAccount());
                billDao.insert(userbill);
            }

        }catch (Exception e){
            return new ResultVo(ResStatus.NO , "fail",null);
        }
        return new ResultVo(ResStatus.OK , "success",null);
    }
}
