package com.java.bms.common.participant.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ParticipantMapper {


    /**
     * 通过用户ID获得会议ID
     * @param commonId 用户ID
     * @return 会议ID
     */
    @Select("select congressId from congressNote where commonId = #{commonId}")
    int getCongressIDByCommonId(int commonId);


    @Insert("insert into congressNote(commonId,congressId) values(#{commonId},#{congressId})")
    int attendCongressByCommonIdAndCongressId(int commonId,int congressId);

}
