package com.java.bms.manager.mapper;


import com.java.bms.hotel.VO.HotelVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ManagerHotelMapper {

    @Select("select * from hotel")
    List<HotelVO> getAllHotel();

    /**
     * 通过酒店ID删除会议申请酒店记录
     * @param hotelId
     * @return
     */
    @Delete("delete from congressApplyHotel where hotelId = #{hotelId}")
    int deleteCongressApplyHotelByHotelId(int hotelId);

    /**
     * 通过酒店ID删除会议酒店
     * @param hotelId
     * @return
     */
    @Delete("delete from congressHotel where hotelId = #{hotelId}")
    int deleteCongressHotelByHotelId(int hotelId);

    /**
     * 删除酒店信息
     * @param hotelId
     * @return
     */
    @Delete("delete from hotel where hotelId = #{hotelId}")
    int deleteHotel(int hotelId);

    /**
     * 通过酒店ID删除用户取消预约记录
     * @param hotelId
     * @return
     */
    @Delete("delete from hotelCancelOrder where hotelId = #{hotelId}")
    int deleteHotelCancelOrderByHotelId(int hotelId);

    /**
     * 通过酒店ID删除用户预约成功记录
     * @param hotelId
     * @return
     */
    @Delete("delete from hotelCheckInNote where hotelId = #{hotelId}")
    int deleteHotelCheckInNoteByHotelId(int hotelId);

    /**
     * 删除酒店登录记录
     * @param hotelId
     * @return
     */
    @Delete("delete from hotelLogin where id = #{hotelId}")
    int deleteHotelLogin(int hotelId);

    /**
     * 删除用户预约失败记录
     * @param hotelId
     * @return
     */
    @Delete("delete from hotelOrderFailNote where hotelId = #{hotelId}")
    int deleteOrderFailNote(int hotelId);

    /**
     * 通过酒店ID删除用户申请预约记录
     * @param hotelId
     * @return
     */
    @Delete("delete from hotelOrderNote where hotelId = #{hotelId}")
    int deleteHotelOrderNote(int hotelId);
}
