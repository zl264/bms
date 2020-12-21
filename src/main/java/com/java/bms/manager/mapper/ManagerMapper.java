package com.java.bms.manager.mapper;

import com.java.bms.common.DO.CongressNoteVO;
import com.java.bms.common.VO.CommonUserVO;
import com.java.bms.common.VO.CongressVO;
import com.java.bms.other.DO.UserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 对于管理员登录的数据库访问的控制
 */
@Mapper
public interface ManagerMapper {
    /**
     * 通过用户名和密码来查找输入的用户名密码是否正确
     * @param username 用户名
     * @param password 密码
     * @return 返回按照用户名和密码得到的用户
     */
    @Select("select * from managerLogin where username=#{username} and password=#{password}")
    UserDO commonLogin(String username, String password);

    /**
     * 查询数据库中的所有会议
     * @return 查询得到的所有会议的List
     */
    @Select("select * from congress")
    List<CongressVO> getAllCongress();

    /**
     * 通过会议ID获得会议全部信息
     * @param congressId 会议ID
     * @return 会议全部信息
     */
    @Select("select * from congress where congressId = #{congressId}")
    CongressVO getCongressById(int congressId);

    /**
     * 通过管理员用户名获得管理员ID
     * @param username 用户名
     * @return 用户ID
     */
    @Select("select id from managerLogin where username = #{username}")
    int getManagerIdByUsername(String username);

    /**
     * 通过用户ID获得用户名
     * @param id 用户ID
     * @return 用户名
     */
    @Select("select username from commonLogin where id = #{id}")
    String getUsernameById(int id);

    /**
     * 通过用户ID和会议ID获取用户参加会议记录
     * @param commonId 用户ID
     * @param congressId 会议ID
     * @return 用户参加会议记录
     */
    @Select("select * from congressNote where commonId = #{commonId} and congressId = #{congressId}")
    CongressNoteVO getCongressNoteByCommonIdAndCongressId(int commonId, long congressId);


//    /**
//     * 通过会议ID获取参与者信息
//     * @param congressId
//     * @return
//     */
//    @Select("select * from congressNote,commonUser where congressNote.congressId = #{congressId} and congressNote.commonId = commonUser.commonId")
//    List<CommonUserVO> getParticipantIdByCongressId(int congressId);
}
