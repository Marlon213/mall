package com.ay.mall.common;

public class Const {

    public static final String CURRENT_USER = "currentUser";

    //一个类型的，枚举过于厚重
    public interface Type{
        String USERNAME="username";
        String EMAIL="email";
    }

    public interface Role{
        int ROLE_CUSTOMER=0;
        int ROLE_ADMIN=1;
    }
}
