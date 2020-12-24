package com.java.bms.hotel.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


/**
 * 预约酒店的VO类
 * 预约成功的VO类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelNoteVO {
    private int hotelId;
    private int commonId;
    private String commonPhone;
    private LocalDateTime time;
    private LocalDateTime checkInStartTime;
    private LocalDateTime checkInEndTime;
    private String commonName;
    private int type;
}
