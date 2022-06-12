package com.mall.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.entity.Comment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentDao extends BaseMapper<Comment> {
    //通过商品id获得评论
    public List<Comment> getCommentByGoodsId(int goodsId);
    //买方查看评论
    public List<Comment> selectUserComment(int userId);
    //卖方查看评论
    public List<Comment> selectSellerComment(int sellerId);
}
