package com.mall.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.mall.dao.*;
import com.mall.entity.*;
import com.mall.service.AdminService;
import com.mall.util.MD5Utils;
import com.mall.vo.ResStatus;
import com.mall.vo.ResultVo;
import com.sun.org.apache.regexp.internal.RE;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminDao adminDao;
    @Autowired
    UserDao userDao;
    @Autowired
    SellerDao sellerDao;
    @Autowired
    GoodsDao goodsDao;
    @Autowired
    CarouselDao carouselDao;
    @Override
    public ResultVo loginAdmin(String adminName , String adminPwd) {
        QueryWrapper<Admin> wrapper = new QueryWrapper<>();
        wrapper.eq("admin_name",adminName);
        //根据管理员姓名获得管理员
        Admin admin = adminDao.selectOne(wrapper);
        if(admin == null){//用户不存在
            return new ResultVo(ResStatus.NO , "用户名不存在", null);
        }else{
            //String md5pwd = MD5Utils.getMD5(adminPwd);
            // 因为没有给管理员提供注册业务，所以这里简单使用明文存管理员的密码
            //但是用户商家有注册业务，在用户商家注册时，会使用md5加密密码
            if(adminPwd.equals(admin.getAdminPassword())){//密码相等
                //准备用JWT生成令牌
                JwtBuilder builder = Jwts.builder();
                HashMap<String ,Object> map = new HashMap<>();
                //给令牌添加角色信息，这里添加的角色为管理员
                map.put("role" , "admin");
                //生成令牌
                String token = builder.setSubject(adminName)
                        .setIssuedAt(new Date())
                        .setId(String.valueOf(admin.getAdminId()))
                        .setClaims(map)
                        .setExpiration(new Date(System.currentTimeMillis() + 24*60*60*1000))//设置令牌过期时间为一天
                        .signWith(SignatureAlgorithm.HS256 , "mall302")//签名，设密码。解码时需要
                        .compact();
                return new ResultVo(ResStatus.OK,token,null);
            }
            else{//密码不等
                return new ResultVo(ResStatus.NO,"用户名或者密码错误",null);
            }
        }
    }

    @Override
    public ResultVo checkUser(User user) {
        int i=0;
        if(user.getUserStatus() == -2){//不通过审核
            i = userDao.deleteById(user.getUserId());
        }else{//其他操作（通过审核、等级调整）
            i = userDao.updateById(user);
        }

        if(i>0){
            return new ResultVo(ResStatus.OK,"操作成功",null);
        }else{
            return new ResultVo(ResStatus.NO,"操作失败",null);
        }
    }

    @Override
    public ResultVo checkSeller(Seller seller) {
        int i=0;
        if(seller.getUserStatus() == -2){//不通过审核
            i = sellerDao.deleteById(seller.getSellerId());
        }else{//其他操作（通过审核、恢复营业，等级调整）
            i = sellerDao.updateById(seller);
        }

        if(i>0){
            return new ResultVo(ResStatus.OK,"操作成功",null);
        }else{
            return new ResultVo(ResStatus.NO,"操作失败",null);
        }
    }

    @Override
    public ResultVo checkGoods(Goods goods) {
        int i=0;
        if(goods.getGoodsStatus() == -2){//不通过审核
            i = goodsDao.deleteById(goods.getGoodsId());
        }else{//其他操作（通过审核、下架、恢复上架）
            i = goodsDao.updateById(goods);
        }
        if(i>0){
            return new ResultVo(ResStatus.OK,"操作成功",null);
        }else{
            return new ResultVo(ResStatus.NO,"操作失败",null);
        }
    }

    @Override
    public ResultVo checkCarousel(Carousel carousel) {
        int i= carouselDao.updateById(carousel);
        if(i>0){
            return new ResultVo(ResStatus.OK,"操作成功",null);
        }else{
            return new ResultVo(ResStatus.NO,"操作失败",null);
        }
    }

    @Override
    @Transactional
    public ResultVo userBlacklist(int userId) {
        try{
            User user = userDao.selectById(userId);
            user.setUserStatus(-1);
            userDao.updateById(user);
            //修改用户发布的所有商品的状态为下架
            UpdateWrapper<Goods> userUpdateWrapper = new UpdateWrapper<>();
            userUpdateWrapper.eq("seller_id",userId)
                    .and(i-> i.eq("seller_type","user"));
            userUpdateWrapper.set("goods_status",-1);
            int result = goodsDao.update(null,userUpdateWrapper);
            return new ResultVo(ResStatus.OK , "success" , result);
        }catch (Exception e){
            e.printStackTrace();
            return new ResultVo(ResStatus.NO , "fail" , null);
        }

    }

    @Override
    @Transactional
    public ResultVo sellerBlacklist(int sellerId) {
        try{
            Seller seller = sellerDao.selectById(sellerId);
            seller.setUserStatus(-1);
            sellerDao.updateById(seller);
            //修改用户发布的所有商品的状态为下架
            UpdateWrapper<Goods> userUpdateWrapper = new UpdateWrapper<>();
            userUpdateWrapper.eq("seller_id",sellerId)
                    .and(i-> i.eq("seller_type","seller"));
            userUpdateWrapper.set("goods_status",-1);
            int result = goodsDao.update(null,userUpdateWrapper);
            return new ResultVo(ResStatus.OK , "success" , result);
        }catch (Exception e){
            e.printStackTrace();
            return new ResultVo(ResStatus.NO , "fail" , null);
        }

    }
}
