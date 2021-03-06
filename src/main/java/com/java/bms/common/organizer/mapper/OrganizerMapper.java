package com.java.bms.common.organizer.mapper;

import com.java.bms.common.DO.ArrivalPlaceCountDO;
import com.java.bms.common.VO.CommonUserAllInformationVO;
import com.java.bms.common.VO.CommonUserVO;
import com.java.bms.common.VO.CongressHaveDriverVO;
import com.java.bms.common.VO.CongressVO;
import com.java.bms.driver.VO.DriverVO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 组织者对数据库操作的mapper接口
 */
@Mapper
@Repository
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
    @Select("select driver.*,congressDriver.place" +
            " from driver,congressDriver " +
            "where congressDriver.congressId = #{congressId} " +
            "and congressDriver.driverId = driver.driverId " +
            "group by driverId")
    List<CongressHaveDriverVO> getDriverByCongressId(int congressId);


    /**
     * 通过会议ID获取会议安排的司机的信息
     * 此方法的返回值用于产看所有司机时判断司机是否已在会议中
     * @param congressId
     * @return
     */
    @Select("select driver.*" +
            " from driver,congressDriver " +
            "where congressDriver.congressId = #{congressId} " +
            "and congressDriver.driverId = driver.driverId ")
    List<DriverVO> getDriverByCongressId1(int congressId);


    /**
     * 通过会议ID获取会议安排的司机的信息,与上面的方法不同之处是返回值不同
     * @param congressId
     * @return
     */
    @Select("select driver.*,congressDriver.place" +
            " from driver,congressDriver " +
            "where congressDriver.congressId = #{congressId} " +
            "and congressDriver.driverId = driver.driverId " +
            "group by driverId")
    List<DriverVO> getDriverByCongressId2(int congressId);

    /**
     * 获取司机安排的人员数量
     * @param driverId
     * @param congressId
     * @return
     */
    @Select("select count(*) from userDriver " +
            "where driverId = #{driverId} and congressId = #{congressId}")
    Integer getDriverListNum(int driverId,int congressId);


    /**
     * 通过会议ID获取会议添加的司机
     * @param congressId
     * @return
     */
    @Select("select driver.* from driver,congressDriver where congressDriver.congressId = #{congressId} " +
            "and congressDriver.driverId = driver.driverId")
    List<DriverVO> getCongressDriverByCongressId(int congressId);


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


    /**
     * 获取哪些地方还要接送，并且那个地方还有多少人需要接送
     * @param congressId
     * @return
     */
    @Select("select arrivalPlace,count(arrivalPlace) num from congressNote " +
            "where congressId = #{congressId} and " +
            "commonId not in(select commonId from userDriver where congressId = #{congressId}) and " +
            "arrivalPlace is not null " +
            "group by arrivalPlace ")
    List<ArrivalPlaceCountDO> getRemainderParticipant(int congressId);

    /**
     * 得到司机已经分配的接送人员名单的数量
     * @param driverId
     * @param congressId
     * @return
     */
    @Select("select count(commonId) from userDriver " +
            "where driverId = #{driverId} and congressId = #{congressId}")
    int getNumberByDriverIdAndCongressId(int driverId,int congressId);

    /**
     * 得到司机要接送的地点
     * @param driverId
     * @param congressId
     * @return
     */
    @Select("select place from congressDriver " +
            "where driverId = #{driverId} and congressId = #{congressId} ")
    String getPlaceByDriverIdAndCongressId(int driverId,int congressId);

    /**
     * 通过会议Id和到达地点得到所有未分配司机的参与者
     * @param arrivalPlace
     * @param congressId
     * @return
     */
    @Select("select commonId from congressNote where arrivalPlace = #{arrivalPlace} " +
            "and congressId = #{congressId} and " +
            "commonId not in(select commonId from userDriver where congressId = #{congressId})")
    List<Integer> getUnassignedCommonIdByArrivalPlace(String arrivalPlace,int congressId);

    /**
     * 给用户分配司机
     * @param commonId
     * @param congressId
     * @param driverId
     * @return
     */
    @Insert("insert userDriver(commonId,congressId,driverId)" +
            " values(#{commonId},#{congressId},#{driverId})")
    int participantAssignedDriver(int commonId,int congressId,int driverId);


    /**
     * 设置司机接送地点
     * @param congressId
     * @param driverId
     * @param place
     * @return
     */
    @Update("update congressDriver set place = #{place}" +
            " where congressId = #{congressId} and driverId = #{driverId}")
    int setPinkUpPlace(int congressId,int driverId,String place);

    /**
     * 通过会议ID和司机Id获取接送人员信息
     * @param congressId
     * @param driverId
     * @return
     */
    @Select("select commonUser.*,congressNote.arrivalPlace,congressNote.arrivalTime" +
            " from commonUser,userDriver,congressNote " +
            "where commonUser.commonId = userDriver.commonId and userDriver.congressId = #{congressId} " +
            "and userDriver.driverId = #{driverId} and congressNote.commonId = commonUser.commonId " +
            "and congressNote.congressId = userDriver.congressId ")
    List<CommonUserAllInformationVO> getList(int congressId, int driverId);

    /**
     * 设置司机接送地点
     * @param congressId
     * @param driverId
     * @param place
     * @return
     */
    @Update("update congressDriver set Place = #{Place}" +
            " where congressId = #{congressId} and driverId = #{driverId}")
    int setDriverPlace(int congressId,int driverId,String place);
}
