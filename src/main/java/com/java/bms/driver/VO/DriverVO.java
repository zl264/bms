package com.java.bms.driver.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverVO {
    private int driverId;
    private String username;
    private String name;
    private String tel;
    private int capacity;
    private String licensePlateNumber;
    private String sex;
    private int age;
    private String idCardNo;
    private String image;
}

