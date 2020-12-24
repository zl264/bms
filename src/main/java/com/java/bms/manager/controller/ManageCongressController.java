package com.java.bms.manager.controller;

import com.java.bms.common.DO.ArrivalPlaceCountDO;
import com.java.bms.common.DO.CongressNoteVO;
import com.java.bms.common.mapper.CommonMapper;
import com.java.bms.common.organizer.mapper.CongressHotelMapper;
import com.java.bms.common.organizer.mapper.OrganizerMapper;
import com.java.bms.common.participant.mapper.ParticipantMapper;
import com.java.bms.driver.VO.DriverVO;
import com.java.bms.manager.mapper.ManageCongressMapping;
import com.java.bms.manager.mapper.ManagerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.java.bms.common.VO.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * 管理员管理会议的controller
 */
@Controller
public class ManageCongressController {

    @Autowired
    ManagerMapper managerMapper;

    @Autowired
    ManageCongressMapping managerCongressMapper;

    @Autowired
    CommonMapper commonMapper;

    @Autowired
    OrganizerMapper organizerMapper;

    @Autowired
    ParticipantMapper participantMapper;

    @Autowired
    CongressHotelMapper congressHotelMapper;

    /**
     * 查看所有会议
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/manager/allCongress")
    public String returnMain(HttpSession session, Model model){
        List<CongressVO> allCongress = managerCongressMapper.getAllCongress();
        model.addAttribute("allCongress", allCongress);
        return "/manager/allCongress";
    }

    /**
     * 查看单独会议
     * @param congressId
     * @param session
     * @param model
     * @param map
     * @return
     */
    @RequestMapping("/manager/lookCongress/{congressId}")
    public String lookCongress(@PathVariable("congressId") int congressId,
                               HttpSession session, Model model, Map<String,Integer> map){
        CongressVO congress = commonMapper.getCongressById(congressId);
        String organizerName = commonMapper.getUsernameById((int) congress.getOrganizerId());

        //获取所有参加会议的参加者
        List<CommonUserVO> participants = commonMapper.getParticipantIdByCongressId((int)congress.getCongressId());
        //获取所有填写了到达时间和到达地点的参加者
        List<CommonUserAllInformationVO> allInformationParticipants = commonMapper.getAllInformationParticipantIdByCongressId((int)(congress.getCongressId()));
        //判断当前参与者是否有填写完个人信息
        CommonUserVO participantInformation = commonMapper.HaveInfomation((String) session.getAttribute("loginUser"));
        //获取每个到达地点的人数
        List<ArrivalPlaceCountDO> allArrivalPlace = commonMapper.getAllParticipantPlaceByCongressId(congressId);
        //获取会议司机
        List<CongressHaveDriverVO> hasDriver = organizerMapper.getDriverByCongressId(congressId);
        for(CongressHaveDriverVO driver:hasDriver){
            map.put(String.valueOf(driver.getDriverId()),organizerMapper.getDriverListNum(driver.getDriverId(),congressId));
        }
        //获取参与者的司机

        List<DriverVO> allDriver = organizerMapper.getAllDriver();
        List<DriverVO> applyDriver = organizerMapper.getApplyDriver(congressId);
        List<DriverVO> hasDriver1 = organizerMapper.getDriverByCongressId1(congressId);

        //得到所有的酒店
        List<CongressHotelVO> allHotel = congressHotelMapper.getAllHotel(congressId);
        //得到已申请的会议
        List<CongressHotelVO> applyHotel = congressHotelMapper.getCongressApplyHotel(congressId);
        //得到会议所有的酒店
        List<CongressHotelVO> hasHotel = congressHotelMapper.getCongressHotel(congressId);

        model.addAttribute("congress", congress);
        model.addAttribute("organizerName", organizerName);
        model.addAttribute("hasDriver",hasDriver);
        model.addAttribute("hasDriver1",hasDriver1);
        model.addAttribute("participants", participants);
        model.addAttribute("participantInformation",participantInformation);
        model.addAttribute("allInformationParticipants",allInformationParticipants);
        model.addAttribute("allArrivalPlace",allArrivalPlace);
        model.addAttribute("driverHaveNum",map);
        model.addAttribute("allDriver",allDriver);
        model.addAttribute("applyDriver",applyDriver);
        model.addAttribute("allHotel",allHotel);
        model.addAttribute("applyHotel",applyHotel);
        model.addAttribute("hasHotel",hasHotel);

        return "/manager/lookCongress";
    }

    /**
     * 删除会议
     * @param congressId
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/manager/deleteCongress/{congressId}")
    public String deleteCongress(@PathVariable("congressId") int congressId,HttpSession session,
                                 Model model){
        managerCongressMapper.deleteCongress(congressId);
        managerCongressMapper.deleteCongressApplyDriverByCongressId(congressId);
        managerCongressMapper.deleteCongressApplyHotelByCongressId(congressId);
        managerCongressMapper.deleteCongressDriverByCongressId(congressId);
        managerCongressMapper.deleteCongressHotelByHotelId(congressId);
        managerCongressMapper.deleteCongressNoteByCongressId(congressId);
        managerCongressMapper.deleteDriverRefuseCongressByCongressId(congressId);
        managerCongressMapper.deleteUserDriverByCongressId(congressId);

        List<CongressVO> allCongress = managerCongressMapper.getAllCongress();
        model.addAttribute("allCongress", allCongress);
        return "/manager/allCongress";
    }



    /**
     * 修改会议信息
     * @param title
     * @param content
     * @param place
     * @param startTimeStr
     * @param endTimeStr
     * @param registerStartTimeStr
     * @param registerEndTimeStr
     * @param congressId
     * @param model
     * @param session
     * @return
     */
    @RequestMapping("/manager/updateCongress")
    public String alterCongress(@RequestParam("title") String title, @RequestParam("content") String content,
                                @RequestParam("place") String place, @RequestParam("congressId") int congressId,
                                @RequestParam("startTime") String startTimeStr, @RequestParam("endTime") String endTimeStr,
                                @RequestParam("registerStartTime") String registerStartTimeStr,
                                @RequestParam("registerEndTime") String registerEndTimeStr,
                                Model model, HttpSession session, Map<String,Integer> map){
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime = LocalDateTime.parse(startTimeStr.replaceAll("T", " ") + ":00", df);
        LocalDateTime endTime = LocalDateTime.parse(endTimeStr.replaceAll("T", " ") + ":00", df);
        LocalDateTime registerStartTime = LocalDateTime.parse(registerStartTimeStr.replaceAll("T", " ") + ":00", df);
        LocalDateTime registerEndTime = LocalDateTime.parse(registerEndTimeStr.replaceAll("T", " ") + ":00", df);
        LocalDateTime now = LocalDateTime.now();
        organizerMapper.alterCongress(congressId,title,content,place,startTime,endTime,registerStartTime,registerEndTime);

        int userId = commonMapper.getCommonIdByUsername((String)session.getAttribute("loginUser"));
        CongressVO congress = commonMapper.getCongressById(congressId);
        String organizerName = commonMapper.getUsernameById((int)congress.getOrganizerId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        CongressNoteVO record = commonMapper.getCongressNoteByCommonIdAndCongressId(userId,congress.getCongressId());
        List<CommonUserVO> participants = commonMapper.getParticipantIdByCongressId(congressId);
        //获取所有填写了到达时间和到达地点的参加者
        List<CommonUserAllInformationVO> allInformationParticipants = commonMapper.getAllInformationParticipantIdByCongressId(congressId);
        //获取每个到达地点的人数
        List<ArrivalPlaceCountDO> allArrivalPlace = commonMapper.getAllParticipantPlaceByCongressId(congressId);

        List<CongressHaveDriverVO> hasDriver = organizerMapper.getDriverByCongressId(congressId);
        for(CongressHaveDriverVO driver:hasDriver){
            map.put(String.valueOf(driver.getDriverId()),organizerMapper.getDriverListNum(driver.getDriverId(),congressId));
        }
        //判断当前参与者是否有填写完个人信息
        CommonUserVO participantInformation = commonMapper.HaveInfomation((String) session.getAttribute("loginUser"));
        List<DriverVO> allDriver = organizerMapper.getAllDriver();
        List<DriverVO> applyDriver = organizerMapper.getApplyDriver(congressId);
        List<DriverVO> hasDriver1 = organizerMapper.getDriverByCongressId1(congressId);

        //得到所有的酒店
        List<CongressHotelVO> allHotel = congressHotelMapper.getAllHotel(congressId);
        //得到已申请的会议
        List<CongressHotelVO> applyHotel = congressHotelMapper.getCongressApplyHotel(congressId);
        //得到会议所有的酒店
        List<CongressHotelVO> hasHotel = congressHotelMapper.getCongressHotel(congressId);

        model.addAttribute("hasDriver",hasDriver);
        model.addAttribute("hasDriver1",hasDriver1);
        model.addAttribute("congress",congress);
        model.addAttribute("organizerName",organizerName);
        model.addAttribute("formatter",formatter);
        model.addAttribute("record",record);
        model.addAttribute("participants",participants);
        model.addAttribute("participantInformation",participantInformation);
        model.addAttribute("allInformationParticipants",allInformationParticipants);
        model.addAttribute("allArrivalPlace",allArrivalPlace);
        model.addAttribute("driverHaveNum",map);
        model.addAttribute("allDriver",allDriver);
        model.addAttribute("applyDriver",applyDriver);
        model.addAttribute("allHotel",allHotel);
        model.addAttribute("applyHotel",applyHotel);
        model.addAttribute("hasHotel",hasHotel);

        //        判断当前时间用户是否可以参加会议
        if(now.isBefore(congress.getRegisterEndTime())&&now.isAfter(congress.getRegisterStartTime())){
            model.addAttribute("canRegisterCongress","yes");
        }
        return "/manager/lookCongress";
    }

}
