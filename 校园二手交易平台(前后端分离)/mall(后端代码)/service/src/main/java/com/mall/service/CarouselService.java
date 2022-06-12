package com.mall.service;

import com.mall.entity.Carousel;
import com.mall.vo.ResultVo;

import java.util.List;

public interface CarouselService {
    //获得待轮播的轮播图
    public ResultVo getCarousel();

    //获得全部轮播图
    public ResultVo getAllCarousel();
}
