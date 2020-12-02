package com.java.bms;

import com.java.bms.other.config.AllConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class BmsApplicationTests {

    @Test
    void contextLoads() {
//        AllConfig.addUrl("/zenglin/zneglin");
//        System.out.println(0);
        System.out.println(new Date().getTime());
    }

}
