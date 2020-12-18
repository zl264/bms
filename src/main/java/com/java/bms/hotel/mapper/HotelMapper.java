package com.java.bms.hotel.mapper;

import com.java.bms.common.VO.CongressVO;
import com.java.bms.hotel.VO.HotelVO;
import com.java.bms.other.DO.UserDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
     *
     * @param hotelName
     * @param hotelId
     * @param name
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
    @Insert("insert into hotel(hotelName,hotelId,hotelPhone,hotelLocation,singleRoomPrice,doubleRoomPrice,totalSingleRoom,remainSingleRoom,totalDoubleRoom,remainDoubleRoom,hotelDescription) values(#{hotelName},#{hotelId}," +
            "#{hotelPhone},#{hotelLocation},#{singleRoomPrice},#{doubleRoomPrice},#{totalSingleRoom},#{remainSingleRoom},#{totalDoubleRoom},#{remainDoubleRoom},#{hotelDescription})")
    int createHotel(String hotelName, long hotelId, String name, String hotelPhone, String hotelLocation, int singleRoomPrice, int doubleRoomPrice, int totalSingleRoom, int remainSingleRoom, int totalDoubleRoom, int remainDoubleRoom, String hotelDescription);

    @Update("update hotel set hotelName=#{hotelName},hotelPhone=#{hotelPhone},hotelLocation=#{hotelLocation},singleRoomPrice=#{singleRoomPrice},doubleRoomPrice=#{doubleRoomPrice},totalSingleRoom=#{totalSingleRoom}," +
            "remainSingleRoom=#{remainSingleRoom},totalDoubleRoom=#{totalDoubleRoom},remainDoubleRoom=#{remainDoubleRoom},hotelDescription=#{hotelDescription} where hotelId=#{hotelId})")
    int updateHotel(String hotelName, long hotelId, String name, String hotelPhone, String hotelLocation, int singleRoomPrice, int doubleRoomPrice, int totalSingleRoom, int remainSingleRoom, int totalDoubleRoom, int remainDoubleRoom, String hotelDescription);

    @Select("select * from hotel where hotelName=#{hotelName}")
    HotelVO HaveHotel(String hotelName) ;
    /**
     * 通过酒店ID获得酒店全部信息
     */
    @Select("select * from hotel where hotelId = #{hotelId}")
    HotelVO getHotelById(int hotelId);

    /**
     * 通过id获得酒店ID
     * @param id
     */
    @Select("select hotelId from hotelLogin where id = #{id}")
    int getHotelIdByLoginId(String id) ;

    /**
     * 通过酒店ID获得酒店名
     */
    @Select("select HotelName from hotelLogin where id = #{id}")
    String getHotelNameById(int id) ;


    /**以下部分关于预定记录
     * 通过酒店Id获取预定记录
     * @param hotelId
     */
    @Select("select HotelName from hotelOrderNote where hotelId = #{hotelId}")
    int getHotelOrderById(String hotelId);
    /**
     * 插入预定酒店预定记录
     * @param hotelId
     * @param commonId
     * @param commonPhone
     * @return
     */
    @Insert("insert into hotelOrderNote(hotelId,commonId,commonPhone) values(#{hotelId},#{commonId},#{commomPhone}")
    int createHotelOrderNote(int hotelId,int commonId,int commonPhone);

    @Select("select * from hotel where hotelId=#{hotelId}")
    HotelVO HaveHotelOrder(int hotelId) ;

    /**
     * 通过酒店ID查找预定记录
     * @param hotelId
     * @param commonId
     * @param commonPhone
     * @return
     */
    @Select("select HotelId from hotelOrderNote(hotelId,commonId,commonPhone) values(#{hotelId},#{commonId},#{commomPhone}")
    int getHotelOrderNote(int hotelId,int commonId,int commonPhone);

    /**
     * 插入预定酒店成功记录
     * @param hotelId
     * @param commonId
     * @param commonPhone
     * @return
     */
    @Insert("insert into hotelCheckInNote(hotelId,commonId,commonPhone) values(#{hotelId},#{commonId},#{commomPhone}")
    int createHotelCheckInNote(int hotelId,int commonId,int commonPhone);

    /**
     * 通过酒店ID查找预定成功记录
     * @param hotelId
     * @param commonId
     * @param commonPhone
     * @return
     */
    @Select("select HotelId from hotelCheckInNote(hotelId,commonId,commonPhone) values(#{hotelId},#{commonId},#{commomPhone}")
    int getHotelCheckInNote(int hotelId,int commonId,int commonPhone);
    /**
     * 插入预定酒店失败记录
     *  @param hotelId
     * @param commonId
     * @param commonPhone
     * @return
     */
    @Insert("insert into hotelOrderFailNote(hotelId,commonId,commonPhone) values(#{hotelId},#{commonId},#{commomPhone}")
    int createHotelOrderFailNote(int hotelId,int commonId,int commonPhone);

    /**
     * 通过酒店ID查找失败记录
     * @param hotelId
     * @param commonId
     * @param commonPhone
     * @return
     */
    @Select("select HotelId from hotelOrderFailNote(hotelId,commonId,commonPhone) values(#{hotelId},#{commonId},#{commomPhone}")
    int getHotelOrderFailNote(int hotelId,int commonId,int commonPhone);

}
