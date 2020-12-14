package com.java.bms.common.organizer.controller;

import com.java.bms.common.DO.CongressNoteVO;
import com.java.bms.common.VO.CommonUserVO;
import com.java.bms.common.VO.CongressVO;
import com.java.bms.common.mapper.CommonMapper;
import com.java.bms.common.organizer.mapper.OrganizerMapper;
import com.java.bms.driver.VO.DriverVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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
                                 @RequestParam("registerStartTimeYear") int registerStartTimeYear,
                                 @RequestParam("registerStartTimeMonth") int registerStartTimeMonth,@RequestParam("registerStartTimeDay") int registerStartTimeDay,
                                 @RequestParam("registerStartTimeHour") int registerStartTimeHour, @RequestParam("registerStartTimeMinute") int registerStartTimeMinute,
                                 @RequestParam("registerEndTimeYear") int registerEndTimeYear, @RequestParam("registerEndTimeMonth") int registerEndTimeMonth,@RequestParam("registerEndTimeDay") int registerEndTimeDay,
                                 @RequestParam("registerEndTimeHour") int registerEndTimeHour, @RequestParam("registerEndTimeMinute") int registerEndTimeMinute,
                                 Model model,HttpSession session){
        LocalDateTime startTime = LocalDateTime.of(startTimeYear,startTimeMonth,startTimeDay,startTimeHour,startTimeMinute,0);
        LocalDateTime endTime = LocalDateTime.of(endTimeYear,endTimeMonth,endTimeDay,endTimeHour,endTimeMinute,0);
        LocalDateTime registerStartTime = LocalDateTime.of(registerStartTimeYear,registerStartTimeMonth,registerStartTimeDay,registerStartTimeHour,registerStartTimeMinute);
        LocalDateTime registerEndTime = LocalDateTime.of(registerEndTimeYear,registerEndTimeMonth,registerEndTimeDay,registerEndTimeHour,registerEndTimeMinute);


        int organizerId = commonMapper.getCommonIdByUsername((String)session.getAttribute("loginUser"));


        organizerMapper.createCongress(organizerId,title,content,place,startTime,endTime,registerStartTime,registerEndTime);

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

    /**
     * 修改会议信息
     * @param title
     * @param content
     * @param place
     * @param startTimeYear
     * @param startTimeMonth
     * @param startTimeDay
     * @param startTimeHour
     * @param startTimeMinute
     * @param endTimeYear
     * @param endTimeMonth
     * @param endTimeDay
     * @param endTimeHour
     * @param endTimeMinute
     * @param congressId
     * @param registerStartTimeYear
     * @param registerStartTimeMonth
     * @param registerStartTimeDay
     * @param registerStartTimeHour
     * @param registerStartTimeMinute
     * @param registerEndTimeYear
     * @param registerEndTimeMonth
     * @param registerEndTimeDay
     * @param registerEndTimeHour
     * @param registerEndTimeMinute
     * @param model
     * @param session
     * @return
     */
    @RequestMapping("/organizer/alter")
    public String alterCongress(@RequestParam("title") String title,@RequestParam("content") String content,
                                @RequestParam("place") String place,@RequestParam("startTimeYear") int startTimeYear,
                                @RequestParam("startTimeMonth") int startTimeMonth,@RequestParam("startTimeDay") int startTimeDay,
                                @RequestParam("startTimeHour") int startTimeHour, @RequestParam("startTimeMinute") int startTimeMinute,
                                @RequestParam("endTimeYear") int endTimeYear, @RequestParam("endTimeMonth") int endTimeMonth,@RequestParam("endTimeDay") int endTimeDay,
                                @RequestParam("endTimeHour") int endTimeHour, @RequestParam("endTimeMinute") int endTimeMinute,@RequestParam("congressId") int congressId,
                                @RequestParam("registerStartTimeYear") int registerStartTimeYear,
                                @RequestParam("registerStartTimeMonth") int registerStartTimeMonth,@RequestParam("registerStartTimeDay") int registerStartTimeDay,
                                @RequestParam("registerStartTimeHour") int registerStartTimeHour, @RequestParam("registerStartTimeMinute") int registerStartTimeMinute,
                                @RequestParam("registerEndTimeYear") int registerEndTimeYear, @RequestParam("registerEndTimeMonth") int registerEndTimeMonth,@RequestParam("registerEndTimeDay") int registerEndTimeDay,
                                @RequestParam("registerEndTimeHour") int registerEndTimeHour, @RequestParam("registerEndTimeMinute") int registerEndTimeMinute,
                                Model model,HttpSession session){
        LocalDateTime startTime = LocalDateTime.of(startTimeYear,startTimeMonth,startTimeDay,startTimeHour,startTimeMinute,0);
        LocalDateTime endTime = LocalDateTime.of(endTimeYear,endTimeMonth,endTimeDay,endTimeHour,endTimeMinute,0);
        LocalDateTime registerStartTime = LocalDateTime.of(registerStartTimeYear,registerStartTimeMonth,registerStartTimeDay,registerStartTimeHour,registerStartTimeMinute);
        LocalDateTime registerEndTime = LocalDateTime.of(registerEndTimeYear,registerEndTimeMonth,registerEndTimeDay,registerEndTimeHour,registerEndTimeMinute);
        LocalDateTime now = LocalDateTime.now();
        organizerMapper.alterCongress(congressId,title,content,place,startTime,endTime,registerStartTime,registerEndTime);

        int userId = commonMapper.getCommonIdByUsername((String)session.getAttribute("loginUser"));
        CongressVO congress = commonMapper.getCongressById(congressId);
        String organizerName = commonMapper.getUsernameById((int)congress.getOrganizerId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        CongressNoteVO record = commonMapper.getCongressNoteByCommonIdAndCongressId(userId,congress.getCongressId());
        List<CommonUserVO> participants = commonMapper.getParticipantIdByCongressId(congressId);
        //获取所有填写了到达时间和到达地点的参加者
        List<CommonUserVO> allInformationParticipants = commonMapper.getAllInformationParticipantIdByCongressId(congressId);
        List<DriverVO> hasDriver = organizerMapper.getDriverByCongressId(congressId);
        //判断当前参与者是否有填写完个人信息
        CommonUserVO participantInformation = commonMapper.HaveInfomation((String) session.getAttribute("loginUser"));

        model.addAttribute("hasDriver",hasDriver);
        model.addAttribute("congress",congress);
        model.addAttribute("organizerName",organizerName);
        model.addAttribute("formatter",formatter);
        model.addAttribute("record",record);
        model.addAttribute("participants",participants);
        model.addAttribute("participantInformation",participantInformation);
        model.addAttribute("allInformationParticipants",allInformationParticipants);
        //        判断当前时间用户是否可以参加会议
        if(now.isBefore(congress.getRegisterEndTime())&&now.isAfter(congress.getRegisterStartTime())){
            model.addAttribute("canRegisterCongress","yes");
        }
        return "/common/congress";
    }


    /**
     * 查看所有司机信息
     * @param congressId
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/organizer/driver/{congressId}")
    public String getAllDriver(@PathVariable("congressId") int congressId, HttpSession session, Model model){
        List<DriverVO> allDriver = organizerMapper.getAllDriver();
        List<DriverVO> applyDriver = organizerMapper.getApplyDriver(congressId);
        List<DriverVO> hasDriver = organizerMapper.getDriverByCongressId(congressId);

        model.addAttribute("allDriver",allDriver);
        model.addAttribute("applyDriver",applyDriver);
        model.addAttribute("hasDriver",hasDriver);
        model.addAttribute("congressId",congressId);
        return "/common/organizer/allDriver";
    }

    /**
     * 会议申请司机
     * @param driverId
     * @param congressId
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/organizer/applyDriver")
    public String addDriver(@RequestParam("driverId") int driverId,@RequestParam("congressId") int congressId,
                            HttpSession session,Model model){
        organizerMapper.applyDriver(congressId,driverId);
        List<DriverVO> allDriver = organizerMapper.getAllDriver();
        List<DriverVO> applyDriver = organizerMapper.getApplyDriver(congressId);
        List<DriverVO> hasDriver = organizerMapper.getDriverByCongressId(congressId);

        model.addAttribute("allDriver",allDriver);
        model.addAttribute("applyDriver",applyDriver);
        model.addAttribute("hasDriver",hasDriver);
        model.addAttribute("congressId",congressId);
        return "/common/organizer/allDriver";
    }

    /**
     * 用户设置到达时间和到达地点
     * @param arrivalPlace
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @param model
     * @param session
     * @param congressId
     * @return
     */
    @ResponseBody
    @RequestMapping("/organizer/arrival/{congressId}")
    public ModelAndView setArrivalInformation(@RequestParam("arrivalPlace") String arrivalPlace, @RequestParam("arrivalTimeYear") int year,
                                              @RequestParam("arrivalTimeMonth") int month, @RequestParam("arrivalTimeDay") int day,
                                              @RequestParam("arrivalTimeHour") int hour, @RequestParam("arrivalTimeMinute") int minute,
                                              ModelAndView model, HttpSession session, @PathVariable("congressId") int congressId){
        LocalDateTime arrivalTime = LocalDateTime.of(year,month,day,hour,minute);
        int commonId = commonMapper.getCommonIdByUsername((String)session.getAttribute("loginUser"));
        organizerMapper.setArrivalTime(congressId,commonId,arrivalPlace,arrivalTime);
        CongressNoteVO record = commonMapper.getCongressNoteByCommonIdAndCongressId(commonId,congressId);
        CongressVO congress = commonMapper.getCongressById(congressId);
        String organizerName = commonMapper.getUsernameById((int) congress.getOrganizerId());
        List<CommonUserVO> participants = commonMapper.getParticipantIdByCongressId(congressId);
        //获取所有填写了到达时间和到达地点的参加者
        List<CommonUserVO> allInformationParticipants = commonMapper.getAllInformationParticipantIdByCongressId(congressId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime now = LocalDateTime.now();
        List<DriverVO> hasDriver = organizerMapper.getDriverByCongressId(congressId);
        //判断当前参与者是否有填写完个人信息
        CommonUserVO participantInformation = commonMapper.HaveInfomation((String) session.getAttribute("loginUser"));


        model.setViewName("/common/congress");
        model.addObject("hasDriver",hasDriver);
        model.addObject("congress", congress);
        model.addObject("organizerName", organizerName);
        model.addObject("formatter", formatter);
        model.addObject("record", record);
        model.addObject("participants", participants);
        model.addObject("participantInformation",participantInformation);
        model.addObject("allInformationParticipants",allInformationParticipants);
//        判断当前时间用户是否可以参加会议
        if(now.isBefore(congress.getRegisterEndTime())&&now.isAfter(congress.getRegisterStartTime())){
            model.addObject("canRegisterCongress","yes");
        }
        return model;
    }

    @RequestMapping("/organizer/delete/participant")
    public String deleteParticipant(@RequestParam("commonId") int commonId,@RequestParam("congressId") int congressId,
                                    Model model,HttpSession session){
        organizerMapper.deleteParticipantFromCongress(commonId,congressId);
        organizerMapper.deleteParticipantFromDriver(commonId,congressId);


        int userId = commonMapper.getCommonIdByUsername((String) session.getAttribute("loginUser"));
        CongressVO congress = commonMapper.getCongressById(congressId);
        String organizerName = commonMapper.getUsernameById((int) congress.getOrganizerId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        CongressNoteVO record = commonMapper.getCongressNoteByCommonIdAndCongressId(userId, congress.getCongressId());
        LocalDateTime now = LocalDateTime.now();
        //获取参加者
        List<CommonUserVO> participants = commonMapper.getParticipantIdByCongressId(congressId);
        //获取所有填写了到达时间和到达地点的参加者
        List<CommonUserVO> allInformationParticipants = commonMapper.getAllInformationParticipantIdByCongressId(congressId);
        //获取司机
        List<DriverVO> hasDriver = organizerMapper.getDriverByCongressId(congressId);
        //判断当前参与者是否有填写完个人信息
        CommonUserVO participantInformation = commonMapper.HaveInfomation((String) session.getAttribute("loginUser"));

        model.addAttribute("hasDriver",hasDriver);
        model.addAttribute("congress", congress);
        model.addAttribute("organizerName", organizerName);
        model.addAttribute("formatter", formatter);
        model.addAttribute("record", record);
        model.addAttribute("participants", participants);
        model.addAttribute("participantInformation",participantInformation);
        model.addAttribute("allInformationParticipants",allInformationParticipants);
//        判断当前时间用户是否可以参加会议
        if(now.isBefore(congress.getRegisterEndTime())&&now.isAfter(congress.getRegisterStartTime())){
            model.addAttribute("canRegisterCongress","yes");
        }

        return "common/congress";
    }

}
