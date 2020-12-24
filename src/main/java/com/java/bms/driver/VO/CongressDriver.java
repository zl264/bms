package com.java.bms.driver.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 会议司机记录的VO类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CongressDriver {
    private int congressId;
    private int driverId;
    private String title;
    private LocalDateTime time;
    private String place;
}
