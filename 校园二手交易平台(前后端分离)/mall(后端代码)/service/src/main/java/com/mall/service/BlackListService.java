package com.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.entity.Black;
import com.mall.vo.ResultVo;
import org.springframework.stereotype.Repository;

@Repository
public interface BlackListService{

    //添加黑名单
    public ResultVo addBlackList(int orderItemId , int sellerId , String sellerType);
    //卖方查询黑名单
    public ResultVo selectBlackList(int sellerId , String sellerType);
    //卖方删除黑名单
    public ResultVo deleteBlackList(int blackId);
}
