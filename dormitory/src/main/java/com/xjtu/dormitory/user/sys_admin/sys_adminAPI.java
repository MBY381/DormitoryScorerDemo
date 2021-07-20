package com.xjtu.dormitory.user.sys_admin;


import com.xjtu.dormitory.common.Result;
import com.xjtu.dormitory.security.login.LoginDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/sys_admin")
public class sys_adminAPI {


    @PostMapping("")
    public Result test(String n){
        return Result.success(n);
    }

}
