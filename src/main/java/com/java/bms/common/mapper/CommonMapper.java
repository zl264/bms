package com.java.bms.common.mapper;

import com.java.bms.common.DO.CongressNoteVO;
import com.java.bms.common.VO.CommonUserVO;
import com.java.bms.common.VO.CongressVO;
import com.java.bms.common.VO.CommonUserVO;
import com.java.bms.other.DO.UserDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 对于普通用户登录注册的数据库访问的控制
 */
@Mapper
public interface CommonMapper {

    /**
     * 通过用户名和密码来查找输入的用户名密码是否正确
     * @param username 用户名
     * @param password 密码
     * @return 返回按照用户名和密码得到的用户
     */
    @Select("select * from commonLogin where username=#{username} and password=#{password}")
    UserDO commonLogin(String username, String password);


    /**
     * 通过用户名判断该用户名有没有被注册
     * @param username 用户名
     * @return 返回用户名或者NULL
     */
    @Select("select * from commonLogin where username=#{username}")
    String isRegister(String username);

    /**
     * 对用户输入的用户名和密码进行注册
     * @param username 用户名
     * @param password 密码
     * @return 返回1或者0
     */
    @Insert("insert into commonLogin(username,password) values(#{username},#{password})")
    int commonRegister(String username,String password);
    /**
     * 提交用户输入的个人信息
     * @param username 用户名
     * @param name 用户姓名
     * @param sex 用户性别
     * @param age 用户年龄
     * @param commonId 用户id
     * @param idCardNo 身份证号
     * @param identity 身份
     */
    @Insert("insert into commonUser(username,name,age,idCardNo,identity,sex,commonId,tel) values(#{username}," +
            "#{name},#{age},#{idCardNo},#{identity},#{sex},#{commonId},#{tel})")
    int createInformation(String username,String name,int age,long idCardNo,String identity,String sex,long commonId,String tel);

    @Update("update commonUser set username = #{username} , name= #{name} ,  age= #{age} , idCardNo=#{idCardNo}," +
            " identity= #{identity} , sex= #{sex} ,tel=#{tel} where commonId = #{commonId}")
    int updateInformation(String username,String name,int age, long idCardNo, String identity,String sex, long commonId,String tel);

    @Select("select * from commonUser where username=#{username}")
    CommonUserVO HaveInfomation(String username);

    /**
     * 查询数据库中的所有会议
     * @return 查询得到的所有会议的List
     */
    @Select("select * from congress order by startTime desc")
    List<CongressVO> getAllCongress();

    /**
     * 通过会议ID获得会议全部信息
     * @param congressId 会议ID
     * @return 会议全部信息
     */
    @Select("select * from congress where congressId = #{congressId}")
    CongressVO getCongressById(int congressId);


    /**
     * 通过用户名获得用户ID
     * @param username 用户名
     * @return 用户ID
     */
    @Select("select id from commonLogin where username = #{username}")
    int getCommonIdByUsername(String username);

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
    CongressNoteVO getCongressNoteByCommonIdAndCongressId(int commonId,long congressId);


    /**
     * 通过会议ID获取参与者信息
     * @param congressId
     * @return
     */
    @Select("select commonUser.* from congressNote,commonUser where congressNote.congressId = #{congressId} and congressNote.commonId = commonUser.commonId")
    List<CommonUserVO> getParticipantIdByCongressId(int congressId);



    /**
     * 通过会议ID获取参与者信息
     * @param congressId
     * @return
     */
    @Select("select commonUser.* from congressNote,commonUser where congressNote.congressId " +
            "= #{congressId} and congressNote.commonId = commonUser.commonId and congressNote.arrivalPlace is not null")
    List<CommonUserVO> getAllInformationParticipantIdByCongressId(int congressId);
}
