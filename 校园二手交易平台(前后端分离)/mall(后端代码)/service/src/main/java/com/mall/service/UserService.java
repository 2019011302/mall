package com.mall.service;

import com.mall.entity.User;
import com.mall.vo.ResultVo;

public interface UserService {
    //登录
    public ResultVo Login(String userName , String userPwd);
    //注册
    public ResultVo Regist(User user );
    //通过用户id查找用户
    public ResultVo getUserByUserId(int userId);
    //更新用户信息
    public ResultVo updateUser(User user);
    //得到全部用户信息
    public ResultVo getAllUsers();
}
