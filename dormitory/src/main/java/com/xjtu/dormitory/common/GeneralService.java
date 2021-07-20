package com.xjtu.dormitory.common;

public interface GeneralService {

    void setAvatar(CurrentUser currentUser, String filename);

    void changePwd(CommonDto dto);

}
