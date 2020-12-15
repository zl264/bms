package com.java.bms.common.participant.mapper;

import com.java.bms.common.VO.CommonUserVO;
import com.java.bms.common.VO.CongressVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
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

}
