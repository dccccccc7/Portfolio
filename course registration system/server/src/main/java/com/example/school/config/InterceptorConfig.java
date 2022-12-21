package com.example.school.config;

import com.example.school.interceptors.JWTInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JWTInterceptor())
                .addPathPatterns("/**")//拦截
                .excludePathPatterns("/user/login")//放行
                .excludePathPatterns("/user/register")//放行
                .excludePathPatterns("/test/**")//放行
                .excludePathPatterns("/doc.html")//美观版swagger
                .excludePathPatterns("/swagger-ui/**", "/swagger-resources/**", "/webjars/**", "/v3/**", "/swagger-ui.html/**");//swagger相关放行
    }
}
