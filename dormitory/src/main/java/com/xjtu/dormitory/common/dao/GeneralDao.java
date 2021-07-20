package com.xjtu.dormitory.common.dao;

import com.xjtu.dormitory.common.CommonDto;
import com.xjtu.dormitory.common.CurrentUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;


@Mapper
public interface GeneralDao {

    @Update(
            "update user set u_avatar =#{avatar} where u_id=#{u_id}"
    )
    void setAvatar(@Param("u_id") String userID, @Param("avatar") String filename);

    @Update(
            "update user set u_pwd = #{u_pwd1} where u_id=#{user_id} "
    )
    void changePwd(CommonDto dto);

}
