package com.mall.service;

import com.mall.entity.Seller;
import com.mall.vo.ResultVo;

public interface SellerService {

    //商家登录
    public ResultVo loginSeller(String name , String pwd);

    //商家注册
    public ResultVo registSeller(Seller seller);

    //获得所有商家
    public ResultVo getAllSeller();

    //根据卖家id和卖家类型获得商家商品
    public ResultVo getGoodsBySellerId(int sellerId , String sellerType);

    //根据id获得商家信息
    public ResultVo getSellerBySellerId(int sellerId);

    //更新商家信息
    public ResultVo updateSeller(Seller seller);
}
