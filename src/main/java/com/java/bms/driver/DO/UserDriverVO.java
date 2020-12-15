package com.java.bms.driver.DO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDriverVO {
    private int commonId;
    private int driverId;
    private int congressId;
    private String username;
    private String tel;
    private LocalDateTime arrivalTime;
}
