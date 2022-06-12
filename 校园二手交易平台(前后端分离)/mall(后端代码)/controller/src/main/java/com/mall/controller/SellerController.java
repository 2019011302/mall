package com.mall.controller;

import com.mall.entity.Seller;
import com.mall.entity.User;
import com.mall.service.SellerService;
import com.mall.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/seller")
@CrossOrigin
@Slf4j
public class SellerController {
    @Autowired
    SellerService sellerService;

    //商家登录
    @GetMapping("/login")
    public ResultVo loginSeller(@RequestParam("name") String name , @RequestParam("pwd")String pwd){
        return sellerService.loginSeller(name , pwd);
    }

    //商家注册
    @PostMapping("/regist")
    public ResultVo registSeller(@RequestBody Seller seller ){
        return sellerService.registSeller(seller);
    }
    //获得商家商品
    @GetMapping("/goodslist")
    public ResultVo getGoodsBySellerId(@RequestParam("sellerId") int sellerId ,  @RequestParam("sellerType")String sellerType){
        return sellerService.getGoodsBySellerId(sellerId , sellerType);
    }

    //获得全部商家信息
    @GetMapping("/list")
    public ResultVo getAllSeller(){
        return sellerService.getAllSeller();
    }

    //根据商家id获得商家信息
    @GetMapping("/info")
    public ResultVo getSellerBySellerId(@RequestParam("id") int sellerId) {
        return sellerService.getSellerBySellerId(sellerId);
    }

    //更新商家信息
    @PutMapping("/update")
    public ResultVo updateSeller(@RequestBody Seller seller){
        return sellerService.updateSeller(seller);
    }
}
