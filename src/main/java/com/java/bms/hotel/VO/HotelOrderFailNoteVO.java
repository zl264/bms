package com.java.bms.hotel.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 预约失败的VO类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelOrderFailNoteVO {
    private int hotelId;
    private int commonId;
    private String hotelPhone;
    private LocalDateTime time;
    private LocalDateTime checkInStartTime;
    private LocalDateTime checkInEndTime;
    private int orRead;
    private String hotelName;
}
