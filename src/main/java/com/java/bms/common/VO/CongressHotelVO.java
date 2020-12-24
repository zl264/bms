package com.java.bms.common.VO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会议已有酒店的VO类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CongressHotelVO {
    private int hotelId;
    private String hotelName;
    private String hotelPhone;

}
