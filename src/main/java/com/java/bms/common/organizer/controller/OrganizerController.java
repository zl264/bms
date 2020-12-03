package com.java.bms.common.organizer.controller;

import com.java.bms.common.VO.CongressVO;
import com.java.bms.common.mapper.CommonMapper;
import com.java.bms.common.organizer.mapper.OrganizerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class OrganizerController {

    @Autowired
    OrganizerMapper organizerMapper;

    @Autowired
    CommonMapper commonMapper;

    /**
     * 跳转到创建会议界面
     * @return 创建会议界面
     */
    @RequestMapping("/organizer/creat")
    public String goCreatCongress(){
        return "common/organizer/createCongress";
    }

    @RequestMapping("/organizer/congress")
    public String createCongress(@RequestParam("title") String title,@RequestParam("content") String content,
                                 @RequestParam("place") String place,@RequestParam("startTimeYear") int startTimeYear,
                                 @RequestParam("startTimeMonth") int startTimeMonth,@RequestParam("startTimeDay") int startTimeDay,
                                 @RequestParam("startTimeHour") int startTimeHour, @RequestParam("startTimeMinute") int startTimeMinute,
                                 @RequestParam("endTimeYear") int endTimeYear, @RequestParam("endTimeMonth") int endTimeMonth,@RequestParam("endTimeDay") int endTimeDay,
                                 @RequestParam("endTimeHour") int endTimeHour, @RequestParam("endTimeMinute") int endTimeMinute,
                                 Model model,HttpSession session){
        LocalDateTime startTime = LocalDateTime.of(startTimeYear,startTimeMonth,startTimeDay,startTimeHour,startTimeMinute,0);
        LocalDateTime endTime = LocalDateTime.of(endTimeYear,endTimeMonth,endTimeDay,endTimeHour,endTimeMinute,0);

        int organizerId = commonMapper.getCommonIdByUsername((String)session.getAttribute("loginUser"));


        organizerMapper.createCongress(organizerId,title,content,place,startTime,endTime);

        List<CongressVO> allCongress = organizerMapper.getCongressByOrganizerId(organizerId);

        model.addAttribute("allCongress",allCongress);
        return "common/organizer/manageCongress";
    }

    @RequestMapping("/organizer/manage")
    public String manageCongress(Model model, HttpSession session){
        int organizerId = commonMapper.getCommonIdByUsername((String)session.getAttribute("loginUser"));
        List<CongressVO> allCongress = organizerMapper.getCongressByOrganizerId(organizerId);
        model.addAttribute("allCongress",allCongress);
        return "common/organizer/manageCongress";
    }

}
