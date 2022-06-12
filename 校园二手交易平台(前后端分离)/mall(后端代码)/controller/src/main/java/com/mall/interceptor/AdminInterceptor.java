package com.mall.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mall.vo.ResStatus;
import com.mall.vo.ResultVo;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class AdminInterceptor  implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //预检,放行options请求
        String method = request.getMethod();
        if("OPTIONS".equalsIgnoreCase(method)){
            return true;
        }
        String token = request.getHeader("token");//请求头传值
        if(token == null){
            ResultVo vo = new ResultVo(ResStatus.NO , "请先登录" , null);
            //提示请先登录
            doResponse(response , vo);
            return false;
        }else{
            //若解码出现异常，说明token不对
            try{
                //验证token
                JwtParser parser = Jwts.parser();
                parser.setSigningKey("mall302");
                //解码
                Jws<Claims> claimsJws = parser.parseClaimsJws(token);
                Claims body = claimsJws.getBody();
                //这里，需要在token中得到角色信息，验证访问人员是否是管理员
                //若是普通用户的资源拦截器，这里只需验证token的合法性，不需要进行下面的操作
                String role = body.get("role" , String.class);
                if(role!=null && role.equals("admin")){
                    return true;
                }else{
                    ResultVo vo = new ResultVo(ResStatus.NO , "权限不足！需要管理员权限" , null);
                    doResponse(response , vo);
                    return false;
                }

            }catch ( ExpiredJwtException e) {
                ResultVo vo = new ResultVo(ResStatus.NO , "登录过期，请登录" , null);
                doResponse(response , vo);
            }catch ( UnsupportedJwtException e){
                ResultVo vo = new ResultVo(ResStatus.NO , "token格式有误，请自重" , null);
                doResponse(response , vo);
            }catch ( Exception e){
                ResultVo vo = new ResultVo(ResStatus.NO , "请登录" , null);
                doResponse(response , vo);
            }
            return false;
        }

    }
    //传值给前端
    private void doResponse(HttpServletResponse response , ResultVo vo) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        String str = new ObjectMapper().writeValueAsString(vo);
        out.print(str);
        out.flush();
        out.close();
    }
}