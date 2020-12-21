package com.java.bms.common.participant.mapper;

import com.java.bms.common.VO.CommonUserVO;
import com.java.bms.common.VO.CongressVO;
import com.java.bms.common.VO.DriverUserVO;
import com.java.bms.hotel.VO.HotelOrderFailNoteVO;
import com.java.bms.hotel.VO.HotelVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ParticipantMapper {


    /**
     * 通过用户ID获得该用户所参加的所有会议
     * @param commonId 用户ID
     * @return 该用户所参加的所有会议
     */
    @Select("select congress.* from congress,congressNote where congressNote.commonId = #{commonId} and congressNote.congressId = congress.congressId")
    List<CongressVO> getCongressByCommonId(int commonId);

    /**
     * 通过用户ID和会议ID确定哪个用户参加哪个会议
     * @param commonId 用户ID
     * @param congressId 会议ID
     * @return 1参加成功 0参加失败
     */
    @Insert("insert into congressNote(commonId,congressId) values(#{commonId},#{congressId})")
    int attendCongressByCommonIdAndCongressId(int commonId,int congressId);


    /**
     * 得到用户分配的司机
     * @param congressId
     * @param commonId
     * @return
     */
    @Select("select userDriver.* , driver.username,driver.tel,congressDriver.time,congressDriver.place " +
            "from userDriver,driver,congressDriver " +
            "where userDriver.congressId = #{congressId} and userDriver.commonId = #{commonId} and " +
            "userDriver.congressId = congressDriver.congressId and userDriver.driverId = " +
            " congressDriver.driverId and userDriver.driverId = driver.driverId")
    DriverUserVO getDriverByCongressIdAndCommonId(int congressId,int commonId);

    /**
     * 得到所有的酒店完整信息
     * @return
     */
    @Select("select * from hotel")
    List<HotelVO> getAllHotelFull();


    /**
     * 得到所有会议提供的酒店
     * @param congressId
     * @return
     */
    @Select("select hotel.* from hotel,congressHotel " +
            "where congressHotel.congressId = #{congressId} and congressHotel.hotelId = hotel.hotelId")
    List<HotelVO> getCongressHotel(int congressId);


    /**
     * 得到用户拒接预约的记录
     * @param commonId
     * @return
     */
    @Select("select * from hotelOrderFailNote where commonId = #{commonId}")
    List<HotelOrderFailNoteVO> getRefuseNote(int commonId);

    /**
     * 得到用户预约成功的酒店
     * @param commonId
     * @return
     */
    @Select("select hotel.* from hotel,hotelCheckInNote " +
            "where hotelCheckInNote.commonId = #{commonId} " +
            "and hotelCheckInNote.hotelId = hotel.hotelId")
    List<HotelVO> getAgreeHotel(int commonId);
}
