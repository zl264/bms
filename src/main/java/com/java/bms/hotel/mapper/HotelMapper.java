package com.java.bms.hotel.mapper;

import com.java.bms.common.VO.CongressVO;
import com.java.bms.hotel.VO.HotelCancelNote;
import com.java.bms.hotel.VO.HotelNoteVO;
import com.java.bms.hotel.VO.HotelOrderFailNoteVO;
import com.java.bms.hotel.VO.HotelVO;
import com.java.bms.other.DO.UserDO;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 对于酒店用户登录注册的数据库访问的控制
 */
@Mapper
public interface HotelMapper {

    /**
     * 通过用户名和密码来查找输入的用户名密码是否正确
     * @param username 用户名
     * @param password 密码
     * @return 返回按照用户名和密码得到的用户
     */
    @Select("select * from hotelLogin where username=#{username} and password=#{password}")
    UserDO commonLogin(String username, String password);

    /**
     * 通过用户名判断该用户名有没有被注册
     * @param username 用户名
     * @return 返回用户名或者NULL
     */
    @Select("select * from hotelLogin where username=#{username}")
    String isRegister(String username);

    /**
     * 对用户输入的用户名和密码进行注册
     * @param username 用户名
     * @param password 密码
     * @return 返回1或者0
     */
    @Insert("insert into hotelLogin(username,password) values(#{username},#{password})")
    int commonRegister(String username,String password);


    /**
     * 通过酒店名判断该酒店有没有被注册
     * hotelName判断
     * @return 返回用户名或者NULL
     */
    @Select("select * from hotel where hotelName=#{hotelName}")
    String isRegisterHotel(String hotelName);

    /**
     * 通过酒店用户名获取酒店ID
     * @param hotelUsername
     * @return
     */
    @Select("select id from hotelLogin where username = #{hotelUsername}")
    int getHotelIdByHotelUsername(String hotelUsername);

    /**
     * 插入酒店信息
     * @param hotelName
     * @param hotelId
     * @param hotelPhone
     * @param hotelLocation
     * @param singleRoomPrice
     * @param doubleRoomPrice
     * @param totalSingleRoom
     * @param remainSingleRoom
     * @param totalDoubleRoom
     * @param remainDoubleRoom
     * @param hotelDescription
     * @return
     */
    @Insert("insert into hotel(hotelName,hotelId,hotelPhone,hotelLocation,singleRoomPrice,doubleRoomPrice,totalSingleRoom,remainSingleRoom,totalDoubleRoom,remainDoubleRoom,hotelDescription)" +
            " values(#{hotelName},#{hotelId}," +
            "#{hotelPhone},#{hotelLocation},#{singleRoomPrice},#{doubleRoomPrice},#{totalSingleRoom},#{remainSingleRoom},#{totalDoubleRoom},#{remainDoubleRoom},#{hotelDescription})")
    int createHotel(String hotelName, long hotelId,String hotelPhone, String hotelLocation, int singleRoomPrice, int doubleRoomPrice, int totalSingleRoom, int remainSingleRoom, int totalDoubleRoom, int remainDoubleRoom, String hotelDescription);

    /**
     * 更新酒店信息
     * @param hotelName
     * @param hotelId
     * @param hotelPhone
     * @param hotelLocation
     * @param singleRoomPrice
     * @param doubleRoomPrice
     * @param totalSingleRoom
     * @param remainSingleRoom
     * @param totalDoubleRoom
     * @param remainDoubleRoom
     * @param hotelDescription
     * @return
     */
    @Update("update hotel set hotelName=#{hotelName},hotelPhone=#{hotelPhone},hotelLocation=#{hotelLocation},singleRoomPrice=#{singleRoomPrice},doubleRoomPrice=#{doubleRoomPrice},totalSingleRoom=#{totalSingleRoom}," +
            "remainSingleRoom=#{remainSingleRoom},totalDoubleRoom=#{totalDoubleRoom},remainDoubleRoom=#{remainDoubleRoom},hotelDescription=#{hotelDescription} where hotelId=#{hotelId})")
    int updateHotel(String hotelName, long hotelId, String hotelPhone, String hotelLocation, int singleRoomPrice, int doubleRoomPrice, int totalSingleRoom, int remainSingleRoom, int totalDoubleRoom, int remainDoubleRoom, String hotelDescription);

    /**
     * 通过酒店名称获取酒店
     * @param hotelName
     * @return
     */
    @Select("select * from hotel where hotelName=#{hotelName}")
    HotelVO HaveHotel(String hotelName) ;

    /**
     * 通过酒店ID获得酒店全部信息
     */
    @Select("select * from hotel where hotelId = #{hotelId}")
    HotelVO getHotelByHotelId(int hotelId);


    /**
     * 通过酒店ID获取酒店预约记录
     * @param hotelId
     * @return
     */
    @Select("select * from hotelOrderNote where hotelId = #{hotelId} order by time")
    List<HotelNoteVO> getHotelOrderNoteByHotelId(int hotelId);


    /**
     * 获取普通用户的酒店预约记录
     * @param hotelId
     * @param commonId
     * @return
     */
    @Select("select * from hotelOrderNote where hotelId = #{hotelId} and commonId = #{commonId}")
    HotelNoteVO getHotelOrderNoteByHotelIdAndCommonId(int hotelId,int commonId);

    /**
     * 删除用户的预约记录
     * @param hotelId
     * @param commonId
     * @return
     */
    @Delete("delete from hotelOrderNote where hotelId = #{hotelId} and commonId = #{commonId}")
    int deleteHotelOrderNoteByHotelIdAndCommonId(int hotelId,int commonId);

    /**
     * 插入用户预约成功的记录
     * @param hotelId
     * @param commonId
     * @param commonPhone
     * @param time
     * @param checkInStartTime
     * @param checkInEndTime
     * @param commonName
     * @return
     */
    @Insert("insert into hotelCheckInNote(hotelId,commonId,commonPhone,time,checkInStartTime,checkInEndTime,commonName)" +
            " values(#{hotelId},{commonId},{commonPhone},{time},{checkInStartTime},{checkInEndTime},{commonName})")
    int insertHotelCheckInNote(int hotelId, int commonId, String commonPhone, LocalDateTime time,LocalDateTime
            checkInStartTime,LocalDateTime checkInEndTime,String commonName);

    /**
     * 删除预约成功记录
     * @param hotelId
     * @param commonId
     * @return
     */
    @Delete("delete from hotelCheckInNote where hotel = #{hotel} and commonId = #{commonId}")
    int deleteHotelCheckInNote(int hotelId,int commonId);

    /**
     * 插入用户预约失败的记录
     * @param hotelId
     * @param commonId
     * @param hotelPhone
     * @param orRead
     * @param time
     * @param checkInStartTime
     * @param checkEndTime
     * @return
     */
    @Insert("insert into hotelOrderFailNote(hotelId,commonId,hotelPhone,orRead,time,checkInStartTime,checkEndTime) " +
            "values(#{hotelId},#{commonId},#{hotelPhone},#{orRead},#{time},#{checkInStartTime},#{checkInEndTime})")
    int insertHotelOrderFailNote(int hotelId,int commonId,String hotelPhone,int orRead,LocalDateTime time,
                                 LocalDateTime checkInStartTime,LocalDateTime checkEndTime);


    /**
     * 通过酒店ID获取酒店预约成功记录
     * @param hotelId
     * @return
     */
    @Select("select * from hotelCheckInNote where hotelId = #{hotelId}")
    List<HotelNoteVO> getHotelCheckInNoteByHotelId(int hotelId);

    /**
     * 通过酒店ID获取取消预约记录
     * @param hotelId
     * @return
     */
    @Select("select * from hotelCancelOrder where hotelId = #{hotelId}")
    List<HotelCancelNote> getHotelCancelOrderByHotelId(int hotelId);

    /**
     * 删除取消预约记录
     * @param hotelId
     * @param commonId
     * @return
     */
    @Insert("delete from hotelCancelOrder where hotelId = #{hotelId} and commonId = #{commonId}")
    int deleteHotelCancelOrderByHotelIdAndCommonId(int hotelId,int commonId);



}
