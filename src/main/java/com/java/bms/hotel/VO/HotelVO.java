package com.java.bms.hotel.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelVO {
    private int hotelId;
    private String hotelName;
    private String hotelPhone;
    private String hotelLocation;
    private int singleRoomPrice;
    private int doubleRoomPrice;
    private int totalSingleRoom;
    private int remainSingleRoom;
    private int totalDoubleRoom;
    private int remainDoubleRoom;
    private String hotelDescription;
    private String image;
}
