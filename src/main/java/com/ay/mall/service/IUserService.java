package com.ay.mall.service;

import com.ay.mall.common.ServerResponse;
import com.ay.mall.pojo.User;

public interface IUserService {
    public ServerResponse<User> login(String username, String password);

    public ServerResponse<String> register(User user);

    public ServerResponse<String> checkVaild(String str,String type);

    public ServerResponse<String> selectQuestionByUsername(String username);

    public ServerResponse<String> checkAnswer(String username,String question,String answer);

    public ServerResponse<String> forgetResetPassword(String username,String password,String token);

    public ServerResponse<String> resetPassword(String oldPassword,String newPassword,User user);

    public ServerResponse<User> updateInformation(User user);

    public ServerResponse<User> getInformation(Integer userId);

}
