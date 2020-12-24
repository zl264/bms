package com.java.bms.manager.mapper;

import com.java.bms.driver.VO.DriverVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 管理司机的mapper
 */
@Mapper
@Repository
public interface ManagerDriverMapper {


    /**
     * 得到所有的司机
     * @return
     */
    @Select("select * from driver")
    List<DriverVO> getAllDriver();

    /**
     * 通过司机ID删除会议申请司机记录
     * @param driverId
     * @return
     */
    @Delete("delete from congressApplyDriver where driverId = #{driverId}")
    int deleteCongressApplyDriverByDriverId(int driverId);

    /**
     * 通过司机ID删除会议已有司机
     * @param driverId
     * @return
     */
    @Delete("delete from congressDriver where driverId = #{driverId}")
    int deleteCongressDriverByDriverId(int driverId);

    /**
     * 删除司机信息
     * @param driverId
     * @return
     */
    @Delete("delete from driver where driverId = #{driverId}")
    int deleteDriver(int driverId);

    /**
     * 删除司机账号
     * @param driverId
     * @return
     */
    @Delete("delete from driverLogin where id = #{driverId}")
    int deleteDriverLogin(int driverId);

    /**
     * 删除司机拒绝会议记录
     * @param driverId
     * @return
     */
    @Delete("delete from driverRefuseCongress where driverId = #{driverId}")
    int deleteDriverRefuseCongressByDriverId(int driverId);

    /**
     * 通过司机Id删除用户接送司机记录
     * @param driverId
     * @return
     */
    @Delete("delete from userDriver where driverId = #{driverId}")
    int deleteUserDriverByDriverId(int driverId);
}
