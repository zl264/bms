package com.java.bms.common.organizer.mapper;

import com.java.bms.common.VO.CongressVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
    @Insert("insert into congress(organizerId,title,content,startTime,endTime) " +
            "values(#{organizerId},#{title},#{content},#{startTime},#{endTime})")
    int createCongress(int organizerId, String title, String content,String place, LocalDateTime startTime,LocalDateTime endTime);

    /**
     * 通过会议Id修改会议的图像
     * @param congressId 会议ID
     * @param image 图片代号
     * @return
     */
    @Update("update congress set image = #{image} where congressId = #{congressId}")
    int updateCongressImage(int congressId,int image);
}
