package com.java.bms.common.participant.controller;

import com.java.bms.common.DO.CongressNoteVO;
import com.java.bms.common.VO.CongressVO;
import com.java.bms.common.mapper.CommonMapper;
import com.java.bms.common.participant.mapper.ParticipantMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.time.format.DateTimeFormatter;

@Controller
public class ParticipantController {

    @Autowired
    ParticipantMapper participantMapper;

    @Autowired
    CommonMapper commonMapper;

    @RequestMapping("/participant/information")
    public String participantInformation(){

        return "common/participant/information";
    }

    @RequestMapping("/participant/congress")
    public String lookCongress(){

        return "/common/participant/lookCongress";
    }

    @RequestMapping("/participant/hotel")
    public String lookHotel(){

        return "/common/participant/lookHotel";
    }

    @RequestMapping("/participant/driver")
    public String lookDriver(){

        return "/common/participant/lookDriver";
    }

    @RequestMapping("/participant/attend")
    public String attendCongress(@RequestParam("congressId")int congressId,
                                 HttpSession session, Model model){
        int commonId = commonMapper.getCommonIdByUsername((String)session.getAttribute("loginUser"));
        try{
            participantMapper.attendCongressByCommonIdAndCongressId(commonId, congressId);
        }catch (Exception e){
            model.addAttribute("error","加入会议失败，请重试！");
        }
        CongressVO congress = commonMapper.getCongressById(congressId);
        String organizerName = commonMapper.getUsernameById((int)congress.getOrganizerId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        model.addAttribute("congress",congress);
        model.addAttribute("organizerName",organizerName);
        model.addAttribute("formatter",formatter);
        model.addAttribute("record",new CongressNoteVO(commonId,congressId));
        return "/common/congress";
    }

}
