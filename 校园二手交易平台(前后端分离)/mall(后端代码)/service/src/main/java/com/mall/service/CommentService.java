package com.mall.service;

import com.mall.entity.Comment;
import com.mall.vo.ResultVo;

public interface CommentService {
    //通过商品id查询商品评价
    public ResultVo getCommentByGoodsId(int goodsId);

    //添加用户评论
    public ResultVo addUserComment(int score1 , int service , String text1 , int orderItemId);
    //添加商家评论
    public ResultVo addSellerComment(int score2 , String text2 , int orderItemId);

    //买方查看评论
    public ResultVo selectUserComment(int userId);
    //卖方查看评论
    public ResultVo selectSellerComment(int sellerId);
}
