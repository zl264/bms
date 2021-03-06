package com.java.bms.common.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


/**
 * 司机接送用户记录的VO类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverUserVO {
    private int commonId;
    private int driverId;
    private int congressId;
    private String username;
    private String tel;
    private LocalDateTime time;
    private String place;
}
