package com.icbcintern.prepaycard.utils;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册TestInterceptor拦截器
        InterceptorRegistration registration = registry.addInterceptor(new Interceptor());
        registration.addPathPatterns("/**"); // 所有路径都被拦截
        String[] pathExcludes = new String[]{"/UserLogin", "/UserRegister", "/merchantLogin", "/merchantRegister", "/operatorLogin", "/operatorRegister"};
        registration.excludePathPatterns(pathExcludes);
    }
}
