package com.java.bms.driver.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
