package com.mall.controller;

import com.mall.entity.Carousel;
import com.mall.entity.Goods;
import com.mall.entity.Seller;
import com.mall.entity.User;
import com.mall.service.AdminService;
import com.mall.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@RestController//@Controller +@ResponseBody, Controller表示这是个控制类，@ResponseBody，将返回的对象转为json格式的数据
@RequestMapping("/admin")//请求的地址
@CrossOrigin//允许跨域访问
@Slf4j//日志
public class AdminController {
    @Autowired//注入
    AdminService adminService;
    //管理员登录
    @GetMapping("/login")//查询
    public ResultVo login(@RequestParam("name") String name , @RequestParam("pwd") String pwd){
        return adminService.loginAdmin(name,pwd);
    }
    //审核用户（审核通过、不通过、拉黑、取消拉黑)
    @PutMapping("/checkuser")//修改
    public ResultVo checkUser(@RequestBody User user){
        return adminService.checkUser(user);
    }
    //审核商家信息
    @PutMapping("/checkseller")
    public ResultVo checkSeller(@RequestBody Seller seller){
        return adminService.checkSeller(seller);
    }

    //审核商品信息
    @PutMapping("/checkgoods")
    public ResultVo checkGoods(@RequestBody Goods goods){
        return adminService.checkGoods(goods);
    }
    //审核轮播图
    @PutMapping("/checkcarousel")
    public ResultVo checkCarousel(@RequestBody Carousel carousel){
        return adminService.checkCarousel(carousel);
    }
    //拉黑用户
    @PutMapping("/userblack")
    public ResultVo userBlacklist(@RequestParam("userId") int userId){
        return adminService.userBlacklist(userId);
    }
    //拉黑商家
    @PutMapping("/sellerblack")
    public ResultVo sellerBlacklist(@RequestParam("sellerId") int sellerId){
        return adminService.sellerBlacklist(sellerId);
    }
}
