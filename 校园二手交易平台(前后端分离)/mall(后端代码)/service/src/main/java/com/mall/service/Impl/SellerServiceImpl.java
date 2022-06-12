package com.mall.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mall.dao.GoodsDao;
import com.mall.dao.SellerDao;
import com.mall.entity.Goods;
import com.mall.entity.Seller;
import com.mall.service.SellerService;
import com.mall.util.MD5Utils;
import com.mall.vo.ResStatus;
import com.mall.vo.ResultVo;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class SellerServiceImpl implements SellerService {
    @Autowired
    SellerDao sellerDao;
    @Autowired
    GoodsDao goodsDao;

    @Override
    public ResultVo loginSeller(String name, String pwd) {
        QueryWrapper<Seller> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name",name);
        //根据姓名获得用户
        Seller seller = sellerDao.selectOne(wrapper);
        if(seller == null){//用户不存在
            return new ResultVo(ResStatus.NO , "用户名不存在", null);
        }else{
            String md5pwd = MD5Utils.getMD5(pwd);
            if(md5pwd.equals(seller.getUserPassword()) && seller.getUserStatus()>0){//密码相等
                //生成令牌
                JwtBuilder builder = Jwts.builder();
                HashMap<String ,Object> map = new HashMap<>();
                String token = builder.setSubject(name)
                        .setIssuedAt(new Date())
                        .setId(String.valueOf(seller.getSellerId()))
                        .setClaims(map)
                        .setExpiration(new Date(System.currentTimeMillis() + 24*60*60*1000))
                        .signWith(SignatureAlgorithm.HS256 , "mall302")
                        .compact();
                return new ResultVo(ResStatus.OK,token,seller);
            }
            else{//密码不等
                return new ResultVo(ResStatus.NO,"用户名或者密码错误",null);
            }
        }
    }

    @Override
    public ResultVo registSeller(Seller seller) {
        QueryWrapper<Seller> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name",seller.getUserName());
        Seller seller_1 = sellerDao.selectOne(wrapper);
        if(seller_1 != null) {//不为空说明已经被注册
            return new ResultVo(ResStatus.NO, "名称已经被注册", null);
        } else{
                seller.setUserStatus(0);
                seller.setUserSale(0);
                seller.setSellerScale(5);
                seller.setUserScore(0);
                seller.setUserImg(seller.getUserName());
                seller.setUserComments(0);
                seller.setSellerPaper(seller.getUserName());
                seller.setSellerIdentityCard(seller.getUserName());
                String md5pwd = MD5Utils.getMD5(seller.getUserPassword());
                seller.setUserPassword(md5pwd);
                int i = sellerDao.insert(seller);
                if(i >0){
                    return new ResultVo(ResStatus.OK , "success" , null);

                }
                return new ResultVo(ResStatus.NO , "fail" , null);
        }
    }

    @Override
    public ResultVo getAllSeller() {
        List<Seller> list = sellerDao.selectList(null);
        if(list == null){
            return new ResultVo(ResStatus.NO , "fail" , null);
        }
        return new ResultVo(ResStatus.OK , "success" , list);
    }
    @Override
    public ResultVo getGoodsBySellerId(int sellerId , String sellerType) {
        QueryWrapper<Goods> wrapper = new QueryWrapper<>();
        wrapper.eq("seller_id" , sellerId)
                .eq("seller_type" ,sellerType );
        List<Goods> list = goodsDao.selectList(wrapper);
        if(list == null){
            return new ResultVo(ResStatus.NO , "fail" , null);
        }
        return new ResultVo(ResStatus.OK , "success" , list);
    }

    @Override
    public ResultVo getSellerBySellerId(int sellerId) {
        QueryWrapper<Seller> wrapper = new QueryWrapper<>();
        wrapper.eq("seller_id" , sellerId);
        Seller seller = sellerDao.selectOne(wrapper);
        if(seller == null){
            return new ResultVo(ResStatus.NO , "fail" , null);
        }
        return new ResultVo(ResStatus.OK , "success" , seller);
    }

    @Override
    public ResultVo updateSeller(Seller seller) {
        int i = sellerDao.updateById(seller);
        if(i>0){
            return new ResultVo(ResStatus.OK , "success",i);
        }else{
            return new ResultVo(ResStatus.NO , "fail",null);
        }
    }
}
