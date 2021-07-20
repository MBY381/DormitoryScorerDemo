package com.xjtu.dormitory.security.login.impl;

import com.xjtu.dormitory.security.login.LoginDto;
import com.xjtu.dormitory.security.login.LoginService;
import com.xjtu.dormitory.security.login.dao.LoginDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {

    @Resource
    private LoginDao loginDao;

    @Override
    public boolean checkLogin(LoginDto dto) {

        int count = loginDao.CheckLogin(dto);
        return count>0;
    }

    @Override
    public String findWeb(LoginDto dto) {
        String web = loginDao.findWebById(dto);
        return web;
    }
}
