package com.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("comments")
public class Comment {
    @TableId(value="comment_id",type = IdType.AUTO)
    private Integer commentId ;
    private Integer commentScore1;
    private Integer commentScore2;
    private String commentText1;
    private String commentText2;
    private Date commentTime1;
    private Date commentTime2;
    @TableField("orderItem_id")
    private Integer orderItemId;
    private Integer commentService;
}
