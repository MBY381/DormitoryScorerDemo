package com.xjtu.dormitory.common.dao;

import com.xjtu.dormitory.common.model.User.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.context.annotation.Bean;

@Mapper
public interface CommonDao {

    @Select("select u_id userId,u_name userName,u_avatar avatar,u_pwd userPwd  from user where u_id = #{userId}")
    User findUserById(String userId);

}
