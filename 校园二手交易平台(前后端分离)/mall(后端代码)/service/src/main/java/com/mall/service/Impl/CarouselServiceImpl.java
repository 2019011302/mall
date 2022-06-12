package com.mall.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mall.dao.CarouselDao;
import com.mall.entity.Carousel;
import com.mall.service.CarouselService;
import com.mall.vo.ResStatus;
import com.mall.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class CarouselServiceImpl implements CarouselService {
    @Autowired
    private CarouselDao carouselDao;
    @Override
    public ResultVo getCarousel() {
        QueryWrapper<Carousel> wrapper = new QueryWrapper<>();
        wrapper.eq("carousel_status",1);
        List<Carousel> list = carouselDao.selectList(wrapper);
        if(list==null){
            return new ResultVo(ResStatus.NO , "fail",null);
        }
        return new ResultVo(ResStatus.OK , "success",list);
    }

    @Override
    public ResultVo getAllCarousel() {
        List<Carousel> list = carouselDao.selectList(null);
        if(list==null){
            return new ResultVo(ResStatus.NO , "fail",null);
        }
        return new ResultVo(ResStatus.OK , "success",list);
    }
}
