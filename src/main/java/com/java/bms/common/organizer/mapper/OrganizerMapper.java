package com.java.bms.common.organizer.mapper;

import com.java.bms.common.VO.CongressVO;
import com.java.bms.driver.VO.DriverVO;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrganizerMapper {


    /**
     * 查找用户创建的所有会议
     * @param organizerId 组织者ID
     * @return 创建的所有会议
     */
    @Select("select * from congress where organizerId = #{organizerId}")
    List<CongressVO> getCongressByOrganizerId(int organizerId);

    /**
     * 创建会议
     * @param organizerId 组织者ID
     * @param title 会议标题
     * @param content 会议内容
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    @Insert("insert into congress(organizerId,title,content,place,startTime,endTime,registerStartTime,registerEndTime) " +
            "values(#{organizerId},#{title},#{content},#{place},#{startTime},#{endTime},#{registerStartTime},#{registerEndTime})")
    int createCongress(int organizerId, String title, String content,String place, LocalDateTime startTime,LocalDateTime endTime,LocalDateTime registerStartTime,LocalDateTime registerEndTime);


    /**
     * 修改会议信息
     * @param congressId 会议ID
     * @param title 会议标题
     * @param content 会议内容
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    @Update("update congress set title = #{title} , content = #{content} , place = #{place} , " +
            "startTime = #{startTime} , endTime = #{endTime} , registerStartTime = #{registerStartTime}, registerEndTime = #{registerEndTime}" +
            "where congressId = #{congressId}")
    int alterCongress(int congressId, String title, String content,String place, LocalDateTime startTime,LocalDateTime endTime,LocalDateTime registerStartTime,LocalDateTime registerEndTime);


    /**
     * 通过会议Id修改会议的图像
     * @param congressId 会议ID
     * @param image 图片代号
     * @return
     */
    @Update("update congress set image = #{image} where congressId = #{congressId}")
    int updateCongressImage(int congressId,int image);

    /**
     * 获取所有的司机信息
     * @return
     */
    @Select("select * from driver")
    List<DriverVO> getAllDriver();

    /**
     * 获取会议正在申请的司机
     * @param congressId
     * @return
     */
    @Select("select driver.* from driver,congressApplyDriver where congressApplyDriver.congressId = #{congressId} " +
            "and congressApplyDriver.driverId = driver.driverId")
    List<DriverVO> getApplyDriver(int congressId);

    /**
     * 通过会议ID获取会议安排的司机的信息
     * @param congressId
     * @return
     */
    @Select("select driver.* from driver,congressDriver where congressDriver.congressId = #{congressId} " +
            "and congressDriver.driverId = driver.driverId")
    List<DriverVO> getDriverByCongressId(int congressId);


    /**
     * 会议申请司机
     * @param congressId
     * @param driverId
     * @return
     */
    @Insert("insert into congressApplyDriver(congressId,driverId)" +
            "values(#{congressId},#{driverId})")
    int applyDriver(int congressId,int driverId);


    /**
     * 设置到达时间
     * @param congressId
     * @param commonId
     * @param arrivalPlace
     * @param arrivalTime
     * @return
     */
    @Update("update congressNote set arrivalPlace = #{arrivalPlace} , arrivalTime = #{arrivalTime}" +
            " where congressId = #{congressId} and commonId = #{commonId}")
    int setArrivalTime(int congressId,int commonId,String arrivalPlace,LocalDateTime arrivalTime);


    /**
     * 删除会议记录中的参与者记录
     * @param commonId
     * @param congressId
     * @return
     */
    @Delete("delete from congressNote where commonId = #{commonId} and congressId = #{congressId}")
    int deleteParticipantFromCongress(int commonId,int congressId);


    /**
     * 删除司机接送名单的用户记录
     * @param commonId
     * @param congressId
     * @return
     */
    @Delete("delete from userDriver where commonId = #{commonId} and congressId = #{congressId}")
    int deleteParticipantFromDriver(int commonId,int congressId);


}
