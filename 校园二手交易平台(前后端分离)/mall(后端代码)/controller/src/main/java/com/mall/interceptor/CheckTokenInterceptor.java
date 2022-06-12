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
public class CheckTokenInterceptor  implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       //预检,放行options请求
        String method = request.getMethod();
        if("OPTIONS".equalsIgnoreCase(method)){
            return true;
        }

//        String token = request.getParameter("token"); 参数传值
        String token = request.getHeader("token");//请求头传值
        if(token == null){
            ResultVo vo = new ResultVo(ResStatus.NO , "请先登录" , null);
            //提示请先登录
            doResponse(response , vo);
            return false;
        }else{
            try{
                //验证token
                JwtParser parser = Jwts.parser();
                parser.setSigningKey("mall302");
                //解码
                Jws<Claims> claimsJws = parser.parseClaimsJws(token);
                //Claims body = claimsJws.getBody();
                //String subject = body.getSubject();
                return true;

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
