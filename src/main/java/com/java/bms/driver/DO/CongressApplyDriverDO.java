package com.java.bms.driver.DO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CongressApplyDriverDO {
    private int congressId;
    private int driverId;
    private String title;
}
