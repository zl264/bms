package com.java.bms.common.mapper;

import com.java.bms.other.DO.UserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 对于普通用户登录注册的数据库访问的控制
 */
@Mapper
public interface CommonMapper {

    /**
     * 通过用户名和密码来查找输入的用户名密码是否正确
     * @param username 用户名
     * @param password 密码
     * @return 返回按照用户名和密码得到的用户
     */
    @Select("select * from commonUser where username=#{username} and password=#{password}")
    public UserDO commonLogin(String username, String password);
}
