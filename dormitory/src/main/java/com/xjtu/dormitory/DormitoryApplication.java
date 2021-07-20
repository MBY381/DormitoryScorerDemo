package com.xjtu.dormitory;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
//@MapperScan({"com.xjtu.dormitory.*.*.dao"})
public class DormitoryApplication {

    public static void main(String[] args) {

        System.out.println("启动中》》》》》》》》》》");
        ApplicationContext ctx = SpringApplication.run(DormitoryApplication.class, args);


    }
}
