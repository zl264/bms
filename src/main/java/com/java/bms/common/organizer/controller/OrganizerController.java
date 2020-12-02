package com.java.bms.common.organizer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class OrganizerController {

    @RequestMapping("/organizer/congress")
    public String createCongress(){
        return "common/organizer/createCongress";
    }


}
