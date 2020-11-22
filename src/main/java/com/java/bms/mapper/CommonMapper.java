package com.java.bms.mapper;

import com.java.bms.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CommonMapper {
    @Select("select * from commonUser where username=#{username} and password=#{password}")
    public User commonLogin(String username,String password);
}
