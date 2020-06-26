package com.ay.mall.dao;

import com.ay.mall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUsername(String username);

    int checkEmail(String email);

    User selectLogin(String username,String password);

    String selectQuestionByUsername(String username);

    int checkAnswer(String username,String question,String answer);

    int updatePassword(@Param("username") String username, @Param("password") String password);

    int checkPassword(@Param("oldPassword") String oldPassword,@Param("id") int username);

    int checkEmailByUserName(@Param("Email") String email,@Param("username")String username);


}