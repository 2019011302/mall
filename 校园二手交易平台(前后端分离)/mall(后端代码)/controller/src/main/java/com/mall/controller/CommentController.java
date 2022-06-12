package com.mall.controller;

import com.mall.entity.Comment;
import com.mall.service.CommentService;
import com.mall.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/comment")
@CrossOrigin
@Slf4j
public class CommentController {
    @Autowired
    CommentService commentService;

    @GetMapping("/goodscomment")
    public ResultVo getCommentByGoodsId(@RequestParam("goodsId") int goodsId){
        return commentService.getCommentByGoodsId(goodsId);
    }

    //买家对卖家评价
    @PostMapping("/adduser")
    public ResultVo addUserComment(int score1 , int service , String text1 , int orderItemId){
        return commentService.addUserComment(score1 , service , text1 , orderItemId);
    }
    //卖家对买家评价
    @PostMapping("/addseller")
    public ResultVo addSellerComment(int score2 , String text2 , int orderItemId){
        return commentService.addSellerComment(score2, text2 , orderItemId);
    }
    //买方的评价
    @GetMapping("usercomment")
    public ResultVo selectUserComment(@RequestParam("userId") int userId){
        return commentService.selectUserComment(userId);
    }

    //卖方的评价
    @GetMapping("sellercomment")
    public ResultVo selectSellerComment(@RequestParam("sellerId") int sellerId){
        return commentService.selectSellerComment(sellerId);
    }
}
