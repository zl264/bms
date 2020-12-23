package com.java.bms.manager.mapper;

import com.java.bms.common.VO.*;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ManageCongressMapping {

    /**
     * 查询数据库中的所有会议
     * @return 查询得到的所有会议的List
     */
    @Select("select * from congress order by startTime desc")
    List<CongressVO> getAllCongress();

    /**
     * 删除会议
     * @param congressId
     * @return
     */
    @Delete("delete from congress where congressId = #{congressId}")
    int deleteCongress(int congressId);

    /**
     * 通过会议Id删除会议申请司机记录
     * @param congressId
     * @return
     */
    @Delete("delete from congressApplyDriver where congressId = #{congressId}")
    int deleteCongressApplyDriverByCongressId(int congressId);

    /**
     * 通过会议ID删除会议申请酒店记录
     * @param congressId
     * @return
     */
    @Delete("delete from congressApplyHotel where congressId = #{congressId}")
    int deleteCongressApplyHotelByCongressId(int congressId);

    /**
     * 通过会议Id删除会议司机
     * @param congressId
     * @return
     */
    @Delete("delete from congressDriver where congressId = #{congressId}")
    int deleteCongressDriverByCongressId(int congressId);

    /**
     * 通过会议ID删除会议预约成功酒店记录
     * @param congressId
     * @return
     */
    @Delete("delete from congressHotel where congressId = #{congressId}")
    int deleteCongressHotelByHotelId(int congressId);

    /**
     * 通过会议ID删除会议参加记录
     * @param congressId
     * @return
     */
    @Delete("delete from congressNote where congressId = #{congressId}")
    int deleteCongressNoteByCongressId(int congressId);

    /**
     * 通过会议Id删除司机拒绝会议记录
     * @param congressId
     * @return
     */
    @Delete("delete from driverRefuseCongress where congressId = #{congressId}")
    int deleteDriverRefuseCongressByCongressId(int congressId);

    /**
     *通过会议ID删除司机接送记录
     * @param congressId
     * @return
     */
    @Delete("delete from userDriver where congressId = #{congressId}")
    int deleteUserDriverByCongressId(int congressId);


}
