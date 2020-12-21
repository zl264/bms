package com.java.bms.manager.manageUser.mapper;

import com.java.bms.manager.VO.CongressVO;
import com.java.bms.manager.VO.UserVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Mapper
public interface ManageUserMapping {

    /**
     * 查找所有的用户
     * return 所有的用户
     */
    @Select("select * from commonUser")
    List<UserVo> getAllUser();

    /**
     * 删除指定的用户登录信息
     */
    @Delete("delete from commonLogin where id = #{commonId} and username = #{username}")
    int deleteCommonUserLogin(int commonId,String username);

    /**
     * 删除指定的用户基本信息
     */
    @Delete("delete from commonUser where id = #{commonId} and username = #{username}")
    int deleteCommonUser(int commonId,String username);

    /**
     * 修改成员信息
     * @return
     */
    @Update("update commonUser set commonId = #{commonId} , username = #{username} , name = #{name} , " +
            "sex = #{sex} , idCardNo = #{idCardNo} , identity = #{identity}, age = #{age},tel=#{tel}")
    int updateCommonUser(int commonId, String username, String name, String sex, String idCardNo,String identity,int age,String tel);


    /**
     * 查找所有的会议
     */
    @Select("select * from congress")
    List<CongressVO> getCongress();
}
