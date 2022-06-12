package com.mall.controller;

import com.mall.entity.User;
import com.mall.service.UserService;
import com.mall.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin
@Slf4j
public class UserController {

    @Autowired
    UserService userService;

    //用户登录
    @GetMapping("/login")
    public ResultVo login(@RequestParam("name") String userName , @RequestParam("pwd") String userPwd){
        return userService.Login(userName , userPwd);
    }
    //用户注册
    @PostMapping("/regist")
    public ResultVo regist(@RequestBody User user){
        return userService.Regist(user);
    }
    //根据用户id获得用户信息
    @GetMapping("/info")
    public ResultVo getUserByUserId(@RequestParam("id") int userId) {
        return  userService.getUserByUserId(userId);
    }
    //更新用户信息
    @PutMapping("/update")
    public ResultVo updateUser(@RequestBody User user){
        return userService.updateUser(user);
    }
    //获得全部用户信息
    @GetMapping("/list")
    public ResultVo getlistUser(){
        return userService.getAllUsers();
    }
}
