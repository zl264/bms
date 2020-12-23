package com.java.bms.common.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
