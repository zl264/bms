package com.java.bms.common.organizer.controller;


import com.java.bms.common.DO.ArrivalPlaceCountDO;
import com.java.bms.common.DO.CongressNoteVO;
import com.java.bms.common.VO.*;
import com.java.bms.common.mapper.CommonMapper;
import com.java.bms.common.organizer.mapper.CongressHotelMapper;
import com.java.bms.common.organizer.mapper.OrganizerMapper;
import com.java.bms.common.participant.mapper.ParticipantMapper;
import com.java.bms.driver.VO.DriverVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * 会议有关酒店的操作
 */
@Controller
public class CongressHotelController {

    @Autowired
    CongressHotelMapper congressHotelMapper;

    @Autowired
    CommonMapper commonMapper;

    @Autowired
    OrganizerMapper organizerMapper;

    @Autowired
    ParticipantMapper participantMapper;

    /**
     * 会议申请酒店
     * @param congressId
     * @param hotelId
     * @param model
     * @param session
     * @param map
     * @return
     */
    @RequestMapping("/organizer/applyHotel")
    public String applyHotel(@RequestParam("congressId") int congressId, @RequestParam("hotelId") int hotelId,
                             Model model, HttpSession session, Map<String,Integer> map){
        congressHotelMapper.insertCongressApplyHotel(congressId,hotelId);

        LocalDateTime now = LocalDateTime.now();
        int userId = commonMapper.getCommonIdByUsername((String)session.getAttribute("loginUser"));
        CongressVO congress = commonMapper.getCongressById(congressId);
        String organizerName = commonMapper.getUsernameById((int)congress.getOrganizerId());
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


        return "/common/congress";
    }
}
