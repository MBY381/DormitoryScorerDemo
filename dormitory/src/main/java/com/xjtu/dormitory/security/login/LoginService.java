package com.xjtu.dormitory.security.login;

public interface LoginService {

    boolean checkLogin(LoginDto dto);

    String findWeb(LoginDto dto);
}
