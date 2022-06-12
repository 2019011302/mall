package com.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("blacklist")
public class Black {
    @TableId(value = "id" , type = IdType.AUTO)
    private Integer id;
    @TableField(value="user_id")
    private Integer userId;
    @TableField(value="seller_id")
    private Integer sellerId;
    @TableField(value="time")
    private Date time;
    @TableField(value="seller_type")
    private String sellerType;
}
