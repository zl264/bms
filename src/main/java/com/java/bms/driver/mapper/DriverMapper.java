package com.java.bms.driver.mapper;

import com.java.bms.driver.DO.CongressApplyDriverDO;
import com.java.bms.driver.VO.UserDriverVO;
import com.java.bms.driver.VO.CongressDriver;
import com.java.bms.driver.VO.DriverVO;
import com.java.bms.other.DO.UserDO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 对于司机用户登录注册的数据库访问的控制
 */
@Mapper
@Repository
public interface DriverMapper {
    /**
     * 通过用户名和密码来查找输入的用户名密码是否正确
     * @param username 用户名
     * @param password 密码
     * @return 返回按照用户名和密码得到的用户
     */
    @Select("select * from driverLogin where username=#{username} and password=#{password}")
    UserDO driverLogin(String username, String password);


    /**
     * 通过用户名判断该用户名有没有被注册
     * @param username 用户名
     * @return 返回用户名或者NULL
     */
    @Select("select * from driverLogin where username=#{username}")
    String isRegister(String username);

    /**
     * 对用户输入的用户名和密码进行注册
     * @param username 用户名
     * @param password 密码
     * @return 返回1或者0
     */
    @Insert("insert into driverLogin(username,password) values(#{username},#{password})")
    int driverRegister(String username,String password);



    /**
     * 通过司机ID获取司机信息
     * @param driverId
     * @return
     */
    @Select("select * from driver where driverId = #{driverId}")
    DriverVO getDriverByDriverId(int driverId);

    /**
     * 填入司机信息
     * @param driverId
     * @param name
     * @param tel
     * @param capacity
     * @param licensePlateNumber
     * @param sex
     * @param age
     * @param idCardNo
     * @return
     */
    @Insert("insert into driver(driverId,username,name,tel,capacity,licensePlateNumber,sex,age,idCardNo)" +
            "values(#{driverId},#{username},#{name},#{tel},#{capacity},#{licensePlateNumber},#{sex},#{age},#{idCardNo})")
    int addDriverInformation(int driverId,String username,String name,String tel,int capacity,String licensePlateNumber,String sex,int age,String idCardNo);



    /**
     * 修改司机信息
     * @param driverId
     * @param name
     * @param tel
     * @param capacity
     * @param licensePlateNumber
     * @param sex
     * @param age
     * @param idCardNo
     * @return
     */
    @Update("update driver set username = #{username},name = #{name}, tel = #{tel},capacity = #{capacity}," +
            "licensePlateNumber = #{licensePlateNumber}, sex = #{sex},age = #{age},idCardNo = #{idCardNo} " +
            " where driverId = #{driverId}")
    int updateDriverInformation(int driverId,String username,String name,String tel,int capacity,String licensePlateNumber,String sex,int age,String idCardNo);

    /**
     *
     * @param username
     * @return
     */
    @Select("select id from driverLogin where username = #{username}")
    int getDriverIdByUsername(String username);

    /**
     * 修改司机信息
     * @param driverId
     * @param name
     * @param tel
     * @param capacity
     * @param licensePlateNumber
     * @param sex
     * @param age
     * @param idCardNo
     * @return
     */
    @Update("update driver set username = #{username},name = #{name}, tel = #{tel},capacity = #{capacity}," +
            "licensePlateNumber = #{licensePlateNumber}, sex = #{sex},age = #{age},idCardNo = #{idCardNo} " +
            " where driverId = #{driverId}")
    int alterDriverInformation(int driverId,String username,String name,String tel,int capacity,String licensePlateNumber,String sex,int age,String idCardNo);


    /**
     * 获取所有申请该司机的会议
     * @param driverId
     * @return
     */
    @Select("select congressApplyDriver.driverId,congress.congressId,congress.title from congressApplyDriver,congress " +
            "where congressApplyDriver.driverId = #{driverId} and congressApplyDriver.congressId = congress.congressId")
    List<CongressApplyDriverDO> getApplyCongressesByDriverId(int driverId);

    /**
     * 通过司机ID获取他要接送的会议,时间地点已经确定好
     * @param driverId
     * @return
     */
    @Select("select congressDriver.driverId,congress.congressId,congress.title,congressDriver.time,congressDriver.place" +
            " from congressDriver,congress " +
            "where congressDriver.driverId = #{driverId} and congressDriver.congressId = congress.congressId " +
            " and congressDriver.place is not null and congressDriver.time is not null")
    List<CongressDriver> getCongressByDriverId(int driverId);

    /**
     * 通过司机ID获取他要接送的会议,时间地点可能未确定好
     * @param driverId
     * @return
     */
    @Select("select congressDriver.driverId,congress.congressId,congress.title,congressDriver.time,congressDriver.place" +
            " from congressDriver,congress " +
            "where congressDriver.driverId = #{driverId} and congressDriver.congressId = congress.congressId")
    List<CongressDriver> getAllCongressByDriverId(int driverId);


    /**
     * 删除会议申请司机记录
     * @param driverId
     * @param congressId
     * @return
     */
    @Delete("delete from congressApplyDriver where driverId = #{driverId} and congressId = #{congressId}")
    int deleteApplyCongressByDriverIdAndCongressId(int driverId,int congressId);


    /**
     * 添加司机要接送的会议的记录
     * @param driverId
     * @param congressId
     * @return
     */
    @Insert("insert into congressDriver(driverId,congressId) " +
            "values(#{driverId},#{congressId})")
    int addCongressDriver(int driverId, int congressId);


    /**
     * 添加司机拒绝会议的记录
     * @param driverId
     * @param congressId
     * @return
     */
    @Insert("insert into driverRefuseCongress(driverId,congressId)" +
            "values(#{driverId},#{congressId})")
    int addDriverRefuseCongress(int driverId,int congressId);

    @Update("update congressDriver set time = #{time} " +
            "where congressId = #{congressId} and driverId = #{driverId}")
    int addTime(int congressId,int driverId,LocalDateTime time);

    /**
     * 通过司机ID和会议ID删除接送任务
     * @param driverId
     * @param congressId
     * @return
     */
    @Delete("delete from congressDriver where driverId = #{driverId} and congressId = #{congressId}")
    int deleteCongressDriver(int driverId,int congressId);

    /**
     * 获取司机接送的人员名单
     * @param driverId
     * @param congressId
     * @return
     */
    @Select("select userDriver.*,commonUser.username,commonUser.tel,congressNote.arrivalTime from userDriver,commonUser,congressNote " +
            "where userDriver.driverId = #{driverId} and userDriver.congressId = #{congressId} " +
            "and userDriver.commonId = commonUser.commonId and congressNote.congressId = #{congressId}" +
            " and congressNote.commonId = userDriver.commonId")
    List<UserDriverVO> getList(int driverId,int congressId);

    /**
     * 判断用户名和手机号是否一致
     * @param username
     * @param tel
     * @return
     */
    @Select("select count(*) from driver where username = #{username} and tel = #{tel}")
    int usernameAndTelIsRight(String username,String tel);


    /**
     * 更新密码
     * @param driverId
     * @param password
     * @return
     */
    @Update("update driverLogin set password = #{password} " +
            "where id = #{driverId}")
    int updatePassword(int driverId,String password);
}
