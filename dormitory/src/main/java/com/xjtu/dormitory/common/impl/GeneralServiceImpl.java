package com.xjtu.dormitory.common.impl;

import com.xjtu.dormitory.common.CommonDto;
import com.xjtu.dormitory.common.CurrentUser;
import com.xjtu.dormitory.common.GeneralService;
import com.xjtu.dormitory.common.dao.GeneralDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service
@Transactional
public class GeneralServiceImpl implements GeneralService {

    @Resource
    private GeneralDao generalDao;


    @Override
    public void setAvatar(CurrentUser currentUser, String filename) {
        generalDao.setAvatar(currentUser.getUserId(),filename);
    }

    @Override
    public void changePwd(CommonDto dto) {
        generalDao.changePwd(dto);
    }
}
