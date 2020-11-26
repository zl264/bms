package com.java.bms.manage.mapper;

import com.java.bms.other.DO.UserDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 对于管理员登录的数据库访问的控制
 */
@Mapper
public interface ManageMapper {
    /**
     * 通过用户名和密码来查找输入的用户名密码是否正确
     * @param username 用户名
     * @param password 密码
     * @return 返回按照用户名和密码得到的用户
     */
    @Select("select * from manageUser where username=#{username} and password=#{password}")
    UserDO commonLogin(String username, String password);


}
