package com.java.bms.common.participant.controller;

import com.java.bms.common.DO.ArrivalPlaceCountDO;
import com.java.bms.common.DO.CongressNoteVO;
import com.java.bms.common.VO.*;
import com.java.bms.common.mapper.CommonMapper;
import com.java.bms.common.organizer.mapper.CongressHotelMapper;
import com.java.bms.common.organizer.mapper.OrganizerMapper;
import com.java.bms.common.participant.mapper.ParticipantMapper;
import com.java.bms.hotel.VO.HotelOrderFailNoteVO;
import com.java.bms.hotel.VO.HotelVO;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 参与者的控制类
 */
@Controller
public class ParticipantController {

    @Autowired
    ParticipantMapper participantMapper;

    @Autowired
    CommonMapper commonMapper;

    @Autowired
    OrganizerMapper organizerMapper;

    @Autowired
    CongressHotelMapper congressHotelMapper;


    /**
     * 查看自己参加的所有会议
     * @param model
     * @param session
     * @return
     */
    @RequestMapping("/participant/congress")
    public String lookCongress(Model model,HttpSession session){
        int commonId = commonMapper.getCommonIdByUsername((String)session.getAttribute("loginUser"));
        CommonUserVO user = commonMapper.getCommonUserByCommonId(commonId);
        List<CongressVO> allCongresses = participantMapper.getCongressByCommonId(commonId);

        model.addAttribute("allCongress",allCongresses);
        model.addAttribute("user",user);
        return "/common/participant/lookCongress";
    }


    /**
     * 加入会议
     * @param congressId 会议ID
     * @param session session
     * @param model 存储信息的结构
     * @return 会议界面
     */
    @RequestMapping("/participant/attend")
    public String attendCongress(@RequestParam("congressId")int congressId,
                                 HttpSession session, Model model){
        int commonId = commonMapper.getCommonIdByUsername((String)session.getAttribute("loginUser"));
        CommonUserVO participantInformation = commonMapper.HaveInfomation((String) session.getAttribute("loginUser"));
        try{
            if(participantInformation!=null)
                participantMapper.attendCongressByCommonIdAndCongressId(commonId, congressId);
        }catch (Exception e){
            model.addAttribute("error","加入会议失败，请重试！");
        }

        CongressVO congress = commonMapper.getCongressById(congressId);
        String organizerName = commonMapper.getUsernameById((int)congress.getOrganizerId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        CongressNoteVO record = commonMapper.getCongressNoteByCommonIdAndCongressId(commonId,congressId);
        LocalDateTime now = LocalDateTime.now();
        //获取参与者的司机
        DriverUserVO participantDriver = participantMapper.getDriverByCongressIdAndCommonId(congressId,commonId);

        model.addAttribute("congress",congress);
        model.addAttribute("organizerName",organizerName);
        model.addAttribute("formatter",formatter);
        model.addAttribute("record",record);
        model.addAttribute("participantInformation",participantInformation);
        model.addAttribute("participantDriver",participantDriver);
        //        判断当前时间用户是否可以参加会议
        if(now.isBefore(congress.getRegisterEndTime())&&now.isAfter(congress.getRegisterStartTime())){
            model.addAttribute("canRegisterCongress","yes");
        }
        return "/common/congress";
    }

    /**
     * 用户设置到达时间和到达地点
     * @param arrivalPlace
     * @param arrivalTimeStr
     * @param model
     * @param session
     * @param congressId
     * @return
     */
    @ResponseBody
    @RequestMapping("/organizer/arrival/{congressId}")
    public ModelAndView setArrivalInformation(@RequestParam("arrivalPlace") String arrivalPlace, @RequestParam("arrivalTime") String arrivalTimeStr,
                                              ModelAndView model, HttpSession session, @PathVariable("congressId") int congressId,
                                              Map<String,Integer> map){
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime arrivalTime = LocalDateTime.parse(arrivalTimeStr.replaceAll("T", " ") + ":00", df);

        int commonId = commonMapper.getCommonIdByUsername((String)session.getAttribute("loginUser"));
        organizerMapper.setArrivalTime(congressId,commonId,arrivalPlace,arrivalTime);
        CongressNoteVO record = commonMapper.getCongressNoteByCommonIdAndCongressId(commonId,congressId);
        CongressVO congress = commonMapper.getCongressById(congressId);
        String organizerName = commonMapper.getUsernameById((int) congress.getOrganizerId());
        List<CommonUserVO> participants = commonMapper.getParticipantIdByCongressId(congressId);
        //获取所有填写了到达时间和到达地点的参加者
        List<CommonUserAllInformationVO> allInformationParticipants = commonMapper.getAllInformationParticipantIdByCongressId(congressId);
        //获取每个到达地点的人数
        List<ArrivalPlaceCountDO> allArrivalPlace = commonMapper.getAllParticipantPlaceByCongressId(congressId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime now = LocalDateTime.now();
        List<CongressHaveDriverVO> hasDriver = organizerMapper.getDriverByCongressId(congressId);
        for(CongressHaveDriverVO driver:hasDriver){
            map.put(String.valueOf(driver.getDriverId()),organizerMapper.getDriverListNum(driver.getDriverId(),congressId));
        }
        //判断当前参与者是否有填写完个人信息
        CommonUserVO participantInformation = commonMapper.HaveInfomation((String) session.getAttribute("loginUser"));
        //获取参与者的司机
        DriverUserVO participantDriver = participantMapper.getDriverByCongressIdAndCommonId(congressId,commonId);

        model.setViewName("/common/congress");
        model.addObject("hasDriver",hasDriver);
        model.addObject("congress", congress);
        model.addObject("organizerName", organizerName);
        model.addObject("formatter", formatter);
        model.addObject("record", record);
        model.addObject("participants", participants);
        model.addObject("participantInformation",participantInformation);
        model.addObject("allInformationParticipants",allInformationParticipants);
        model.addObject("allArrivalPlace",allArrivalPlace);
        model.addObject("driverHaveNum",map);
        model.addObject("participantDriver",participantDriver);

//        判断当前时间用户是否可以参加会议
        if(now.isBefore(congress.getRegisterEndTime())&&now.isAfter(congress.getRegisterStartTime())){
            model.addObject("canRegisterCongress","yes");
        }
        return model;
    }


    /**
     * 退出会议
     * @param congressId
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/participant/exit")
    public String exitCongress(@RequestParam("congressId") int congressId,HttpSession session, Model model) {
        int commonId = commonMapper.getCommonIdByUsername((String)session.getAttribute("loginUser"));
        CommonUserVO participantInformation = commonMapper.HaveInfomation((String) session.getAttribute("loginUser"));
        organizerMapper.deleteParticipantFromCongress(commonId,congressId);
        organizerMapper.deleteParticipantFromDriver(commonId,congressId);


        CongressVO congress = commonMapper.getCongressById(congressId);
        String organizerName = commonMapper.getUsernameById((int)congress.getOrganizerId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        CongressNoteVO record = commonMapper.getCongressNoteByCommonIdAndCongressId(commonId,congressId);
        LocalDateTime now = LocalDateTime.now();
        //获取参与者的司机
        DriverUserVO participantDriver = participantMapper.getDriverByCongressIdAndCommonId(congressId,commonId);

        model.addAttribute("congress",congress);
        model.addAttribute("organizerName",organizerName);
        model.addAttribute("formatter",formatter);
        model.addAttribute("record",record);
        model.addAttribute("participantInformation",participantInformation);
        model.addAttribute("participantDriver",participantDriver);
        //        判断当前时间用户是否可以参加会议
        if(now.isBefore(congress.getRegisterEndTime())&&now.isAfter(congress.getRegisterStartTime())){
            model.addAttribute("canRegisterCongress","yes");
        }
        return "/common/congress";
    }


    /**
     * 查看酒店
     * @return
     */
    @RequestMapping("/participant/hotel")
    public String lookHotel(HttpSession session,Model model){
        String username = (String)session.getAttribute("loginUser");
        int commonId = commonMapper.getCommonIdByUsername(username);
        CommonUserVO user = commonMapper.getCommonUserByCommonId(commonId);
        List<CongressVO> congress = participantMapper.getCongressByCommonId(commonId);
        //参加的未结束的会议
        List<CongressVO> exitCongress = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for(int i=0;i<congress.size();i++){
            if(now.isBefore(congress.get(i).getEndTime()));
                exitCongress.add(congress.get(i));
        }
        //每个会议提供的酒店
        Map<Object,List<HotelVO>> congressHotels = new HashMap<>();
        for(int i=0;i<exitCongress.size();i++){
            List<HotelVO> hotelVOS = participantMapper.getCongressHotel((int)exitCongress.get(i).getCongressId());
            congressHotels.put(exitCongress.get(i),hotelVOS);
        }
        //所有的酒店
        List<HotelVO> allHotel = participantMapper.getAllHotelFull();
//        //预约失败的记录
//        List<HotelOrderFailNoteVO> failNotes = participantMapper.getRefuseNote(commonId);
        //预约成功的酒店
        List<HotelVO> successHotel = participantMapper.getAgreeHotel(commonId);

        model.addAttribute("user",user);
        model.addAttribute("exitCongress",exitCongress);
        model.addAttribute("congressHotels",congressHotels);
        model.addAttribute("allHotel",allHotel);
//        model.addAttribute("failNotes",failNotes);
        model.addAttribute("successHotel",successHotel);
        model.addAttribute("commonId",commonId);
        return "/common/participant/lookHotel";
    }

}
