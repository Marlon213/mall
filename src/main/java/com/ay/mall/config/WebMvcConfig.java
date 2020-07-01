package com.ay.mall.config;

import com.ay.mall.config.interceptor.LoginHandlerInteceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginHandlerInteceptor()).addPathPatterns("/**")
                .excludePathPatterns("/user/register","/user/login","/user/valid",
                        "/user/question","/user/check_answer","/user/forget_reset_password","/manage/user/login"
                ,"/cart/get_cart_product_count");
    }
}
