package com.xjtu.dormitory.security.login.dao;

import com.xjtu.dormitory.security.login.LoginDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface LoginDao {

    @Select("select count(u_id) from user where u_id=#{u_id} and u_pwd = #{u_pwd}")
    int CheckLogin(LoginDto dto);

    @Select("select role_web from user,role_model where user.role_id=role_model.role_id and user.u_id = #{u_id}")
    String findWebById(LoginDto dto);
}
