package com.ay.mall.controller.portal;

import com.ay.mall.common.Const;
import com.ay.mall.common.ResponseCode;
import com.ay.mall.common.ServerResponse;
import com.ay.mall.pojo.User;
import com.ay.mall.service.IUserService;
import com.ay.mall.util.CookieUtil;
import com.ay.mall.util.JSONUtil;
import com.ay.mall.util.RedisShardedPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user/")
public class UserController {
    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "login",method = RequestMethod.POST)
    public ServerResponse<User> login(String username, String password, HttpSession session, HttpServletResponse httpResponse){
        ServerResponse<User> response =  iUserService.login(username, password);
        if (response.isSuccess()){
//            session.setAttribute(Const.CURRENT_USER,response.getData());
            RedisShardedPoolUtil.set(session.getId(), JSONUtil.obj2String(response.getData()));
            CookieUtil.writeLoginToken(httpResponse,session.getId());
        }
        return response;
    }

    @RequestMapping(value = "logout",method = RequestMethod.GET)
    public ServerResponse<String> logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
//        session.removeAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        CookieUtil.delLoginToken(httpServletRequest,httpServletResponse);
        RedisShardedPoolUtil.del(loginToken);
        return ServerResponse.createBySuccess("退出成功");
    }

    @RequestMapping(value = "register",method = RequestMethod.POST)
    public ServerResponse<String> register(User user){
        return iUserService.register(user);
    }

    @RequestMapping(value = "valid",method= RequestMethod.POST)
    public ServerResponse<String> checkVaild(String str,String type){
        return iUserService.checkVaild(str,type);
    }

    @RequestMapping(value = "information",method = RequestMethod.GET)
    public ServerResponse<User> getInformation(HttpServletRequest httpServletRequest){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JSONUtil.string2Obj(userJsonStr,User.class);
        return ServerResponse.createBySuccess(user);
    }

    @RequestMapping(value = "question",method = RequestMethod.GET)
    public ServerResponse<String> selectQuestionByUsername(String username){
        return iUserService.selectQuestionByUsername(username);
    }

    @RequestMapping(value = "check_answer",method = RequestMethod.GET)
    public ServerResponse<String> checkAnswer(String username,String question,String answer){
        return iUserService.checkAnswer(username,question,answer);
    }

    @RequestMapping(value = "forget_reset_password",method = RequestMethod.POST)
    public ServerResponse<String> forgetResetPassword(String username,String password,String token){
        return iUserService.forgetResetPassword(username, password, token);
    }

    @RequestMapping(value = "reset_password",method = RequestMethod.POST)
    public ServerResponse<String> resetPassword(String oldPassword,String newPassword,HttpServletRequest httpServletRequest){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JSONUtil.string2Obj(userJsonStr,User.class);
        return iUserService.resetPassword(oldPassword,newPassword,user);
    }

    @RequestMapping(value = "update_information",method = RequestMethod.POST)
    public ServerResponse<User> updateInformation(User user,HttpServletRequest httpServletRequest){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User currentUser = JSONUtil.string2Obj(userJsonStr,User.class);
        user.setId(currentUser.getId());
        ServerResponse<User> response = iUserService.updateInformation(user);
        if (response.isSuccess()){
            response.getData().setUsername(currentUser.getUsername());


            RedisShardedPoolUtil.set(loginToken, JSONUtil.obj2String(response.getData()));
          //  session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }

    @RequestMapping(value = "get_information",method = RequestMethod.GET)
    public ServerResponse<User> get_information(HttpServletRequest httpServletRequest){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User currentUser = JSONUtil.string2Obj(userJsonStr,User.class);
        return iUserService.getInformation(currentUser.getId());
    }
}
