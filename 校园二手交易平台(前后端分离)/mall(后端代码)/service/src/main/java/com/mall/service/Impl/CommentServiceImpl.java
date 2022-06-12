package com.mall.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mall.dao.*;
import com.mall.entity.*;
import com.mall.service.CommentService;
import com.mall.vo.ResStatus;
import com.mall.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentDao commentDao;
    @Autowired
    OrderItemDao orderItemDao;
    @Autowired
    GoodsDao goodsDao;
    @Autowired
    UserDao userDao;
    @Autowired
    SellerDao sellerDao;

    @Override
    public ResultVo getCommentByGoodsId(int goodsId) {
        List<Comment> list  = commentDao.getCommentByGoodsId(goodsId);
        if(list == null){
            return new ResultVo(ResStatus.NO , "fail" , null);
        }
        return new ResultVo(ResStatus.OK , "success" , list);
    }

    //买家对卖家评价
    @Override
    @Transactional
    public ResultVo addUserComment(int score1 , int service , String text1 , int orderItemId) {//添加用户评价
        //判断该订单项是否已经被评价。
        OrderItem orderItem = orderItemDao.getOrderItemById(orderItemId);
        int isComment = 0;//是否评价
        int i = 0 ;
        //根据订单项查找评论
        //若评论不存在，则添加
        if(orderItem.getOrderItemIsComment() == 0){
            Comment comment = new Comment();
            comment.setCommentScore1(score1);
            comment.setCommentService(service);
            comment.setCommentText1(text1);
            comment.setCommentTime1(new Date());
            comment.setOrderItemId(orderItemId);
            i = commentDao.insert(comment);
            isComment = 1;
        }else if(orderItem.getOrderItemIsComment() == 2){
            QueryWrapper<Comment> wrapper = new QueryWrapper<>();
            wrapper.eq("orderItem_id" ,orderItemId);
            Comment comment = commentDao.selectOne(wrapper);
            //添加用户评价
            comment.setCommentScore1(score1);
            comment.setCommentService(service);
            comment.setCommentText1(text1);
            comment.setCommentTime1(new Date());
            i = commentDao.updateById(comment);
            isComment = 3;
        }
        if(i>0){
            //修改订单项的评论状态 未评价 0 ， 用户已评价1 ， 商家已评价2 ， 用户和商家都已经评价3
            orderItem.setOrderItemIsComment(isComment);
            orderItemDao.updateOrderItem(orderItem);
            //修改商品评分和商品好评率
            Goods goods = goodsDao.selectById(orderItem.getGoodsId());
            //商品评分=平均分=((总销量-新销量)*评分+新评分*新销量）/总销量
            //商品好评率 好评数量占总评价数的百分比
            //保留两位小数
            DecimalFormat df = new DecimalFormat("#.##");

            //计算商品评分，保留两位小数
            double new_sore = ((goods.getGoodsSales() - orderItem.getGoodsNumber() )*goods.getGoodsScore()+score1 *orderItem.getGoodsNumber())/goods.getGoodsSales();
            double new_sore1 =  Double.parseDouble(df.format(new_sore));
            //计算商品好评率 保留两位小数。好评率 = 好评数量 / 总的评价数量。好评数量 = 好评率 * 总的评价数量

            //获得商品的评价信息
            List<Comment> commentList = commentDao.getCommentByGoodsId(orderItem.getGoodsId());

            int priceNumber = commentList.size()-1;//总的评价数量
            double goodPrice = goods.getGoodsPraiserate();//商品好评率
            //好评数量
            int goodPriceNumber = (int)goodPrice* priceNumber;
            if(score1 == 100){
                goodPriceNumber++;
            }
            priceNumber++;
            if(priceNumber==0){
                priceNumber=1;
            }
            //计算新的好评率,保留两位小数
            double x = (double) goodPriceNumber;
            double y = (double)priceNumber;
            double new_goodPrice1 = x/y;
            double new_goodPrice = Double.parseDouble(df.format(new_goodPrice1));
            //修改商品信息
            goods.setGoodsScore(new_sore1);
            goods.setGoodsPraiserate(new_goodPrice);
            goodsDao.updateById(goods);
            //修改卖家的评分
            if("user".equals(goods.getSellerType())){//是个人用户
                User user = userDao.getSellerByOrderItemId(orderItemId);
                //新总分
                double sum = user.getUserComments() *user.getUserScore() + service;
                //新平均分
                double userScore = sum/(1+user.getUserComments());
                //保留两位小数
                double score = Double.parseDouble(df.format(userScore));
                //修改用户好评率和被评价的数量
                user.setUserScore(score);
                user.setUserComments(user.getUserComments()+1);
                userDao.updateById(user);
            }else{//是商家用户
                Seller seller = sellerDao.getSellerByOrderItemId(orderItemId);
                //新总分
                double sum = seller.getUserComments() *seller.getUserScore() + service;
                //新平均分
                double sellerScore = sum/(1+seller.getUserComments());
                //保留两位小数
                double score = Double.parseDouble(df.format(sellerScore));
                //修改用户好评率和被评价的数量
                seller.setUserScore(score);
                seller.setUserComments(seller.getUserComments()+1);
                sellerDao.updateById(seller);
            }
            return new ResultVo(ResStatus.OK , "success" , null);
        }
        return new ResultVo(ResStatus.NO , "fail" , null);
    }

    //卖家对买家评价
    @Override
    public ResultVo addSellerComment(int score2, String text2, int orderItemId) {
        //判断该订单项是否已经被评价。
        OrderItem orderItem = orderItemDao.getOrderItemById(orderItemId);
        int isComment = 0;//是否评价
        int i = 0 ;
        //根据订单项查找评论
        //若评论不存在，则添加
        try{
            //未被评论
        if(orderItem.getOrderItemIsComment() == 0){
            Comment comment = new Comment();
            comment.setCommentScore2(score2);
            comment.setCommentText2(text2);
            comment.setCommentTime2(new Date());
            comment.setOrderItemId(orderItemId);
            i = commentDao.insert(comment);
            isComment = 1;
        }else if(orderItem.getOrderItemIsComment() == 1){
            QueryWrapper<Comment> wrapper = new QueryWrapper<>();
            wrapper.eq("orderItem_id" ,orderItemId);
            Comment comment = commentDao.selectOne(wrapper);
            //添加用户评价
            comment.setCommentScore2(score2);
            comment.setCommentText2(text2);
            comment.setCommentTime2(new Date());
            i = commentDao.updateById(comment);
            isComment = 3;
        }
            //修改订单项的评论状态 未评价 0 ， 用户已评价1 ， 商家已评价2 ， 用户和商家都已经评价3
            orderItem.setOrderItemIsComment(isComment);
            orderItemDao.updateOrderItem(orderItem);
            //修改用户好评率，算平均分
            User user = userDao.getUserByOrderItemId(orderItemId);
            //新总分
            double sum = user.getUserComments() *user.getUserScore() + score2;
            //新平均分
            double userScore = sum/(1+user.getUserComments());
            //保留两位小数
            DecimalFormat df = new DecimalFormat("#.##");
            double score = Double.parseDouble(df.format(userScore));
            //修改用户好评率和被评价的数量
            user.setUserScore(score);
            user.setUserComments(user.getUserComments()+1);
            userDao.updateById(user);
             return new ResultVo(ResStatus.OK , "success" , null);
        }catch (Exception e){
            e.printStackTrace();
            return new ResultVo(ResStatus.NO , "fail" , null);
        }
    }

    @Override
    public ResultVo selectUserComment(int userId) {
        List<Comment> list = commentDao.selectUserComment(userId);
        if(list == null){
            return new ResultVo(ResStatus.NO , "fail" , null);
        }
        return new ResultVo(ResStatus.OK , "success" , list);
    }

    @Override
    public ResultVo selectSellerComment(int sellerId) {
        List<Comment> list = commentDao.selectSellerComment(sellerId);
        if(list == null){
            return new ResultVo(ResStatus.NO , "fail" , null);
        }
        return new ResultVo(ResStatus.OK , "success" , list);
    }
}
