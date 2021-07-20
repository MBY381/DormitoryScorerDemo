package com.xjtu.dormitory.security.login;


import com.xjtu.dormitory.common.Result;
import com.xjtu.dormitory.common.TokenUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/security/login")
public class LoginAPI {

    @Resource
    private LoginService loginService;

    @PostMapping("")
    public Result Login(@RequestBody LoginDto dto){

        boolean ok = loginService.checkLogin(dto);

        if(ok){

            String role_web = loginService.findWeb(dto);

            //生成登陆令牌
            String token = TokenUtils.loginSign(dto.getU_id(),dto.getU_pwd());
            System.out.println("___________________________");
            return Result.success(role_web,(Object)token);// send success msg and token


        }

        return Result.fail(Result.ERR_CODE_BUSINESS," 账号或密码错误");

    }
}
