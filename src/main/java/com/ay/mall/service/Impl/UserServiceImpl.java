package com.ay.mall.service.Impl;

import com.ay.mall.common.CacheToken;
import com.ay.mall.common.Const;
import com.ay.mall.common.RedisShardedPool;
import com.ay.mall.common.ServerResponse;
import com.ay.mall.dao.UserMapper;
import com.ay.mall.pojo.User;
import com.ay.mall.service.IUserService;
import com.ay.mall.util.MD5Util;
import com.ay.mall.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("iUserService")
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    public ServerResponse<User> login(String username, String password){
        if (userMapper.checkUsername(username)==0){
            return ServerResponse.createByErrorMessage("登录名不存在");
        }
        User user = userMapper.selectLogin(username, MD5Util.MD5EncodeUtf8(password));
        if (user==null){
            return ServerResponse.createByErrorMessage("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登陆成功",user);
    }

    public ServerResponse<String> register(User user){
        ServerResponse<String> validResponse = checkVaild(user.getUsername(),Const.Type.USERNAME);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        validResponse=checkVaild(user.getEmail(),Const.Type.EMAIL);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        user.setRole(Const.Role.ROLE_CUSTOMER);
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        if (userMapper.insert(user)==0){
            return ServerResponse.createByErrorMessage("注册失败!");
        }
        return ServerResponse.createBySuccessMessage("注册成功！");
    }

    public ServerResponse<String> checkVaild(String str,String type){
        if (StringUtils.isNotBlank(str)){
            if (Const.Type.USERNAME.equalsIgnoreCase(type)){
                int count = userMapper.checkUsername(str);
                if (count>0){
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }
            if (Const.Type.EMAIL.equalsIgnoreCase(type)){
                int count = userMapper.checkEmail(str);
                if (count>0){
                    return ServerResponse.createByErrorMessage("邮箱已使用");
                }
            }
        }else {
            return ServerResponse.createByErrorMessage("参数不能为空");
        }
        return ServerResponse.createBySuccessMessage("检验成功");
    }

    public ServerResponse<String> selectQuestionByUsername(String username){
        ServerResponse validResponse = checkVaild(username,Const.Type.USERNAME);
        if (validResponse.isSuccess()){
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isBlank(question)){
            return ServerResponse.createByErrorMessage("该用户未设置找回密码的问题");
        }
        return ServerResponse.createBySuccess(question);
    }

    public ServerResponse<String> checkAnswer(String username,String question,String answer){
        if (userMapper.checkAnswer(username,question,answer)==0){
            return ServerResponse.createByErrorMessage("答案错误");
        }
        String token = UUID.randomUUID().toString();
       // CacheToken.setKey(CacheToken.TOKEN_PREFIX+username,token);
        RedisShardedPoolUtil.setEx(Const.TOKEN_PREFIX+username,token,60*60*12);
        return ServerResponse.createBySuccess(token);
    }

    public ServerResponse<String> forgetResetPassword(String username,String password,String token){
        if (StringUtils.isBlank(username)){
            return ServerResponse.createByErrorMessage("用户名不能为空");
        }
        if (StringUtils.isBlank(token)){
            return ServerResponse.createByErrorMessage("token不能为空");
        }
      //  String cacheToken = CacheToken.getKey(CacheToken.TOKEN_PREFIX+username);
        String cacheToken = RedisShardedPoolUtil.get(Const.TOKEN_PREFIX+username);
        if (StringUtils.isBlank(cacheToken)){
            return ServerResponse.createByErrorMessage("token已过期");
        }
        if (!StringUtils.equals(cacheToken,token)){
            return ServerResponse.createByErrorMessage("token错误,请重新获取");
        }
        if (userMapper.updatePassword(username,MD5Util.MD5EncodeUtf8(password))==0){
            return ServerResponse.createByErrorMessage("修改密码失败");
        }
        return ServerResponse.createBySuccessMessage("修改成功");
    }

    public ServerResponse<String> resetPassword(String oldPassword,String newPassword,User user){
        if (userMapper.checkPassword(MD5Util.MD5EncodeUtf8(oldPassword),user.getId())==0){
            return ServerResponse.createByErrorMessage("用户输入密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(newPassword));
        if (userMapper.updateByPrimaryKeySelective(user)==0){
            return ServerResponse.createByErrorMessage("更新失败");
        }
        return ServerResponse.createBySuccessMessage("更新成功");
    }

    public ServerResponse<User> updateInformation(User user){
        if (userMapper.checkEmailByUserName(user.getEmail(),user.getUsername())>0){
            return ServerResponse.createByErrorMessage("邮箱已注册");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        if (userMapper.updateByPrimaryKeySelective(updateUser)==0){
            return ServerResponse.createByErrorMessage("更新个人信息失败");
        }
        return ServerResponse.createBySuccess("更新成功",updateUser);
    }

    public ServerResponse<User> getInformation(Integer userId){
        User user = userMapper.selectByPrimaryKey(userId);
        if(user == null){
            return ServerResponse.createByErrorMessage("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }


}
