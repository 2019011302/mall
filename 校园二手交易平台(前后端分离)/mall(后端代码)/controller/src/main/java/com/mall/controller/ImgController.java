package com.mall.controller;

import com.mall.service.ImgService;
import com.mall.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/img")
@CrossOrigin
public class ImgController {

    @Autowired
    ImgService imgService;
    @PostMapping("/upload")
   public ResultVo upload(@RequestParam("file") MultipartFile file, @RequestParam("name")String name, @RequestParam("path")String path){
        return imgService.upload(file , name , path);
    }
}
