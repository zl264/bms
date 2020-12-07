package com.java.bms.common.organizer.controller;

import com.java.bms.common.DO.CongressNoteVO;
import com.java.bms.common.VO.CommonUserVO;
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
import java.time.format.DateTimeFormatter;
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
        return "/common/organizer/createCongress";
    }


    /**
     * 创建会议并回到创建会议界面
     * @param title 会议标题
     * @param content 会议内容
     * @param place 会议地点
     * @param startTimeYear 下面五个参数是开始时间
     * @param startTimeMonth
     * @param startTimeDay
     * @param startTimeHour
     * @param startTimeMinute
     * @param endTimeYear 接下来五个参数是结束时间
     * @param endTimeMonth
     * @param endTimeDay
     * @param endTimeHour
     * @param endTimeMinute
     * @param model 传递所有用户创建的会议
     * @param session session
     * @return 回到创建会议界面
     */
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
        return "/common/organizer/manageCongress";
    }

    /**
     * 创建会议界面，获取会议组织者创建的会议
     * @param model 存储组织者组织的会议
     * @param session session
     * @return 创建会议界面
     */
    @RequestMapping("/organizer/manage")
    public String manageCongress(Model model, HttpSession session){
        int organizerId = commonMapper.getCommonIdByUsername((String)session.getAttribute("loginUser"));
        List<CongressVO> allCongress = organizerMapper.getCongressByOrganizerId(organizerId);
        model.addAttribute("allCongress",allCongress);
        return "/common/organizer/manageCongress";
    }


    @RequestMapping("/organizer/alter")
    public String alterCongress(@RequestParam("title") String title,@RequestParam("content") String content,
                                @RequestParam("place") String place,@RequestParam("startTimeYear") int startTimeYear,
                                @RequestParam("startTimeMonth") int startTimeMonth,@RequestParam("startTimeDay") int startTimeDay,
                                @RequestParam("startTimeHour") int startTimeHour, @RequestParam("startTimeMinute") int startTimeMinute,
                                @RequestParam("endTimeYear") int endTimeYear, @RequestParam("endTimeMonth") int endTimeMonth,@RequestParam("endTimeDay") int endTimeDay,
                                @RequestParam("endTimeHour") int endTimeHour, @RequestParam("endTimeMinute") int endTimeMinute,@RequestParam("congressId") int congressId,
                                Model model,HttpSession session){
        LocalDateTime startTime = LocalDateTime.of(startTimeYear,startTimeMonth,startTimeDay,startTimeHour,startTimeMinute,0);
        LocalDateTime endTime = LocalDateTime.of(endTimeYear,endTimeMonth,endTimeDay,endTimeHour,endTimeMinute,0);

        organizerMapper.alterCongress(congressId,title,content,place,startTime,endTime);

        int userId = commonMapper.getCommonIdByUsername((String)session.getAttribute("loginUser"));
        CongressVO congress = commonMapper.getCongressById(congressId);
        String organizerName = commonMapper.getUsernameById((int)congress.getOrganizerId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        CongressNoteVO record = commonMapper.getCongressNoteByCommonIdAndCongressId(userId,congress.getCongressId());
        List<CommonUserVO> participants = commonMapper.getParticipantIdByCongressId(congress.getOrganizerId());


        model.addAttribute("congress",congress);
        model.addAttribute("organizerName",organizerName);
        model.addAttribute("formatter",formatter);
        model.addAttribute("record",record);
        model.addAttribute("participants",participants);
        return "/common/congress";
    }

}
