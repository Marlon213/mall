package com.ay.mall.config.interceptor;


import com.ay.mall.common.Const;
import com.ay.mall.common.ServerResponse;
import com.ay.mall.pojo.User;
import com.ay.mall.util.CookieUtil;
import com.ay.mall.util.JSONUtil;
import com.ay.mall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Component
@Slf4j
public class LoginHandlerInteceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean result = true;
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)){
            result=false;
        }else{
            String userJsonStr = RedisShardedPoolUtil.get(loginToken);
            User user = JSONUtil.string2Obj(userJsonStr, User.class);
            if (user==null) result=false;
        }
        if (!result){
            //重置response
            response.reset();
            //设置编码格式
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter pw = response.getWriter();
            ObjectMapper mapper=new ObjectMapper();
            String str = mapper.writeValueAsString(
                     ServerResponse.createByErrorMessage("请返回登录页面"));
            pw.write(str);
            pw.flush();
            pw.close();
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
