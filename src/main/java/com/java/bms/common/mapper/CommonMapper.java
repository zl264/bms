package com.java.bms.common.mapper;

import com.java.bms.common.bean.CongressVO;
import com.java.bms.other.DO.UserDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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
    @Select("select * from commonLogin where username=#{username} and password=#{password}")
    UserDO commonLogin(String username, String password);


    /**
     * 通过用户名判断该用户名有没有被注册
     * @param username 用户名
     * @return 返回用户名或者NULL
     */
    @Select("select * from commonLogin where username=#{username}")
    String isRegister(String username);

    /**
     * 对用户输入的用户名和密码进行注册
     * @param username 用户名
     * @param password 密码
     * @return 返回1或者0
     */
    @Insert("insert into commonLogin(username,password) values(#{username},#{password})")
    int commonRegister(String username,String password);

    /**
     * 查询数据库中的所有会议
     * @return 查询得到的所有会议的List
     */
    @Select("select * from congress")
    List<CongressVO> getAllCongress();

    @Select("select * from congress where congressId = #{congressId}")
    CongressVO getCongressById(int congressId);
}
