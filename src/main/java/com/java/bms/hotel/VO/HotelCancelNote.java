package com.java.bms.hotel.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelCancelNote {
    private int hotelId;
    private int commonId;
    private String commonPhone;
    private String commonName;
}
