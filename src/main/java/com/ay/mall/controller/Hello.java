package com.ay.mall.controller;

import com.ay.mall.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello {
    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/hello")
    public void hello(){
        System.out.println(userMapper.selectByPrimaryKey(1));
    }
}
