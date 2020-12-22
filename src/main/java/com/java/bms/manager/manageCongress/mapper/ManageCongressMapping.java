package com.java.bms.manager.manageCongress.mapper;

import com.java.bms.common.VO.*;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ManageCongressMapping {

    /**
     * 查询数据库中的所有会议
     * @return 查询得到的所有会议的List
     */
    @Select("select * from congress order by startTime desc")
    List<CongressVO> getAllCongress();

}
