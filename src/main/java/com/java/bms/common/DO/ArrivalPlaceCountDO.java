package com.java.bms.common.DO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 到达地点以及其人数的DO类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArrivalPlaceCountDO {
    private String arrivalPlace;
    private int num;
}
