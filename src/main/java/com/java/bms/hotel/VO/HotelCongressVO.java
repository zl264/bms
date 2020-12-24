package com.java.bms.hotel.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 酒店拥有会议的VO类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelCongressVO {
    private int congressId;
    private int hotelId;
    private String title;
}
