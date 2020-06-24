package com.ay.mall;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@SpringBootTest
class MallApplicationTests {

    @Test
    void contextLoads() throws ClassNotFoundException, SQLException {

        String url="jdbc:mysql://127.0.0.1:3306/mall?characterEncoding=utf-8";
        String user="root";
        String password="123456";
        Connection con= DriverManager.getConnection(url,user,password);
        System.out.println(con);
    }



}
