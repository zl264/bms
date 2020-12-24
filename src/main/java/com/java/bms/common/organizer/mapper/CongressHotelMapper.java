package com.java.bms.common.organizer.mapper;

import com.java.bms.common.VO.CongressHotelVO;
import com.java.bms.hotel.VO.HotelVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 会议酒店的mapper接口
 */
@Mapper
@Repository
public interface CongressHotelMapper {

    /**
     * 得到会议已有的酒店（酒店信息不完全）
     * 用于会议界面判断这个就是是否已申请等等
     * @param congressId
     * @return
     */
    @Select("select hotel.hotelId, hotel.hotelName,hotelPhone " +
            "from hotel")
    List<CongressHotelVO> getAllHotel(int congressId);


    /**
     * 得到会议申请的酒店
     * @param congressId
     * @return
     */
    @Select("select congressApplyHotel.hotelId, hotel.hotelName,hotel.hotelPhone " +
            "from congressApplyHotel,hotel" +
            " where congressApplyHotel.congressId = #{congressId} and congressApplyHotel.hotelId = hotel.hotelId")
    List<CongressHotelVO> getCongressApplyHotel(int congressId);


    /**
     * 得到会议已有的酒店
     * @param congressId
     * @return
     */
    @Select("select congressHotel.hotelId, hotel.hotelName,hotel.hotelPhone " +
            "from congressHotel,hotel" +
            " where congressHotel.congressId = #{congressId} and congressHotel.hotelId = hotel.hotelId")
    List<CongressHotelVO> getCongressHotel(int congressId);


    /**
     * 添加会议申请酒店记录
     * @param congressId
     * @param hotelId
     * @return
     */
    @Insert("insert into congressApplyHotel(congressId,hotelId)" +
            " values(#{congressId},#{hotelId})")
    int insertCongressApplyHotel(int congressId,int hotelId);
}
