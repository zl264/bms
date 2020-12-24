package com.java.bms.common.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 会议有的司机信息的VO类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CongressHaveDriverVO {
    private int driverId;
    private String username;
    private String name;
    private String tel;
    private int capacity;
    private String licensePlateNumber;
    private String sex;
    private int age;
    private String idCardNo;
    private String place;
}
