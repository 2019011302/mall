package com.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("carousel")
public class Carousel {
    @TableId(value="carousel_id", type = IdType.AUTO)
    private Integer carouselId;
    private String carouselImg;
    private Integer carouselStatus;
    private String carouselBgColor;
    private Integer goodsId;
}
