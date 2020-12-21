package com.java.bms;

import com.java.bms.hotel.VO.HotelNoteVO;
import com.java.bms.hotel.mapper.HotelMapper;
import com.java.bms.other.config.AllConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;

@SpringBootTest
class BmsApplicationTests {

    @Autowired
    HotelMapper hotelMapper;

    @Test
    void contextLoads() {
//        AllConfig.addUrl("/zenglin/zneglin");
////        System.out.println(0);
//        System.out.println(new Date().getTime());
//        System.out.println(new ArrayList<>());
        HotelNoteVO hotelNoteVO = hotelMapper.getSingleHotelCheckInNote(1,1);
        System.out.println(hotelMapper.getSingleHotelCheckInNote(1,1));
    }

}
