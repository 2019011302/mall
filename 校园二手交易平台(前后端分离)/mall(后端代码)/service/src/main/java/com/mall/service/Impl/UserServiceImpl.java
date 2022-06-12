package com.mall.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mall.dao.UserDao;
import com.mall.entity.User;
import com.mall.service.UserService;
import com.mall.util.MD5Utils;
import com.mall.vo.ResStatus;
import com.mall.vo.ResultVo;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.management.Query;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl  implements UserService {
    @Autowired
    private UserDao userDao;
    @Override
    public ResultVo Login(String userName, String userPwd) {
       // User user = userDao.searchUser(userName);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name" , userName);
        User user = userDao.selectOne(wrapper);
        if(user == null){//用户不存在
            return new ResultVo(ResStatus.NO , "用户名不存在", null);
        }else{
            String md5pwd = MD5Utils.getMD5(userPwd);
            if(md5pwd.equals(user.getUserPassword()) && user.getUserStatus()>0){//密码相等 //且用户状态大于0
                //生成令牌
                JwtBuilder builder = Jwts.builder();
                HashMap<String ,Object> map = new HashMap<>();
                String token = builder.setSubject(userName)
                        .setIssuedAt(new Date())
                        .setId(String.valueOf(user.getUserId()))
                        .setClaims(map)
                        .setExpiration(new Date(System.currentTimeMillis() + 24*60*60*1000))
                        .signWith(SignatureAlgorithm.HS256 , "mall302")
                        .compact();
                return new ResultVo(ResStatus.OK,token,user);
            }
            else{//密码不等
                return new ResultVo(ResStatus.NO,"用户名或者密码错误",null);
            }
        }
    }

    @Override
    public ResultVo Regist( User user ) {
        //User user = userDao.searchUser(userName);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name",user.getUserName());
        User user1 = userDao.selectOne(wrapper);
        if(user1 != null ) {//不为空说明已经被注册
            return new ResultVo(ResStatus.NO, "用户名已经被注册", null);
        }
        else{
            user.setUserStatus(0);
            user.setUserSale(0);
            //user.setUserScale(5);
            user.setUserScore(0);
            user.setUserImg(user.getUserName());
            user.setUserRewardpoints(0);
            user.setUserComments(0);
            String md5pwd = MD5Utils.getMD5(user.getUserPassword());
            user.setUserPassword(md5pwd);
            int i = userDao.insert(user);
            if(i > 0){
                return new ResultVo(ResStatus.OK , "注册成功",user);
            }else{
                return new ResultVo(ResStatus.NO , "注册失败",null);
            }
        }
    }

    @Override
    public ResultVo getUserByUserId(int userId) {
        User user = userDao.selectById(userId);
        if(user != null){
            return new ResultVo(ResStatus.OK , "success",user);
        }else{
            return new ResultVo(ResStatus.NO , "fail",null);
        }
    }

    @Override
    public ResultVo updateUser(User user) {
        int i = userDao.updateById(user);
        if(i>0){
            return new ResultVo(ResStatus.OK , "success",i);
        }else{
            return new ResultVo(ResStatus.NO , "fail",null);
        }
    }

    @Override
    public ResultVo getAllUsers() {
        List<User> list = userDao.selectList(null);
        if(list != null){
            return new ResultVo(ResStatus.OK , "success",list);
        }else{
            return new ResultVo(ResStatus.NO , "fail",null);
        }
    }
}
