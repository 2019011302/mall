package com.mall.controller;

import com.mall.service.CarouselService;
import com.mall.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@CrossOrigin
@RequestMapping("/carousel")
@Slf4j
public class CarouselController {

    @Autowired
    CarouselService carouselService;

    @GetMapping("/index")
    public ResultVo getIndexCarousel(){
        return carouselService.getCarousel();
    }

    @GetMapping("/list")
    public ResultVo getAllCarousel(){
        return carouselService.getAllCarousel();
    }

}
