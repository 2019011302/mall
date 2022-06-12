package com.mall.config;

import com.mall.interceptor.AdminInterceptor;
import com.mall.interceptor.CheckTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private CheckTokenInterceptor checkTokenInterceptor;//普通用户资源拦截器
    @Autowired
    private AdminInterceptor adminInterceptor;//管理员资源拦截器

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        this.checkTokenInterceptor(registry);//注册普通用户资源拦截器
        this.adminInterceptor(registry);//注册管理员资源拦截器
    }
    //为普通用户资源拦截器，添加拦截的路径
    private void checkTokenInterceptor(InterceptorRegistry registry){
                registry.addInterceptor(checkTokenInterceptor)
                .addPathPatterns("/shopcar/**")
                .addPathPatterns("/order/**")
                .addPathPatterns("/refund/**")
                .addPathPatterns("/bill/**")
                .addPathPatterns("/user/**")
                .addPathPatterns("/seller/**")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/regist")
                        .excludePathPatterns("/seller/login")
                        .excludePathPatterns("/seller/regist");
    }
    //为管理员资源拦截器，添加拦截的路径
    private void adminInterceptor(InterceptorRegistry registry){
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/admin/**")
                .addPathPatterns("/user/list")
                .addPathPatterns("/seller/list")
                .addPathPatterns("/goods/list")
                .addPathPatterns("/tariff/list")
                .addPathPatterns("/carousel/list")
                .excludePathPatterns("/admin/login");
    }
}
