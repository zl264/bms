package com.java.bms.hotel.HotelDO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 不管什么类型的用户注册后都可以只有用户名和密码，其他信息可以之后填写
 * 所以设置这个UserVO类用来登录和注册
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelDo {
    private String getHotelId;
    private String getHotelPhone;
    private Integer id;

    public String getHotelId() {
        return null;
    }

    public String getHotelPhone() {
        return null;
    }
}