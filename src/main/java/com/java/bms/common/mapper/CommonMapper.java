package com.java.bms.common.mapper;

import com.java.bms.other.DO.UserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CommonMapper {
    @Select("select * from commonUser where username=#{username} and password=#{password}")
    public UserDO commonLogin(String username, String password);
}
