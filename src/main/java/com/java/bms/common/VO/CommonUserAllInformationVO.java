package com.java.bms.common.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 普通用户所有信息的VO类
 * 包含会议到达时间和到达地点
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonUserAllInformationVO {
    private String username;
    private String name;
    private int age;
    private String sex;
    private String idCardNo;
    private String identity;
    private int commonId;
    private String tel;
    private String arrivalPlace;
    private LocalDateTime arrivalTime;
    private String image;
}
