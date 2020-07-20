package com.ay.mall.config;

import com.ay.mall.config.interceptor.AuthorityInterceptor;
import com.ay.mall.config.interceptor.LoginHandlerInteceptor;
import com.ay.mall.controller.common.SessionExpireFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginHandlerInteceptor()).addPathPatterns("/**")
                .excludePathPatterns("/user/register","/user/login","/user/valid",
                        "/user/question","/user/check_answer","/user/forget_reset_password","/manage/**"
                ,"/cart/get_cart_product_count");
        registry.addInterceptor(new AuthorityInterceptor()).addPathPatterns("/manage/**")
                .excludePathPatterns("/manage/user/login");
    }


    @Bean
    public FilterRegistrationBean filterRegist() {
        FilterRegistrationBean frBean = new FilterRegistrationBean();
        frBean.setFilter(new SessionExpireFilter());
//        frBean.setOrder(1);//多个过滤器时指定过滤器的执行顺序
        frBean.addUrlPatterns("/**");

        log.info("Session Expire Filter");
        return frBean;
    }
}
