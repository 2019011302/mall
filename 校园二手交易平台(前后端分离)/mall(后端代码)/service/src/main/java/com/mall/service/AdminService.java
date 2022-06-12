package com.mall.service;

import com.mall.entity.Carousel;
import com.mall.entity.Goods;
import com.mall.entity.Seller;
import com.mall.entity.User;
import com.mall.vo.ResultVo;

public interface AdminService {
    //管理员登录
    public ResultVo loginAdmin(String adminName , String adminPwd);
    //审核用户（用户审核、取消拉黑、修改用户等级)
    public ResultVo checkUser(User user);
    //审核商家（商家审核、恢复营业、修改等级)
    public ResultVo checkSeller(Seller seller);
    //审核商品(商品的审核、下架、上架)
    public ResultVo checkGoods(Goods goods);
    //审核轮播图
    public ResultVo checkCarousel(Carousel carousel);
    //拉黑用户，同时下架用户的所有商品
    public ResultVo userBlacklist(int userId);
    //拉黑商家，通过下架商家的所有商品
    public ResultVo sellerBlacklist(int sellerId);
}
