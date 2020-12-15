package com.java.bms.common.participant.controller;

import com.java.bms.common.DO.CongressNoteVO;
import com.java.bms.common.VO.CommonUserVO;
import com.java.bms.common.VO.CongressVO;
import com.java.bms.common.mapper.CommonMapper;
import com.java.bms.common.participant.mapper.ParticipantMapper;
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
public class ParticipantController {

    @Autowired
    ParticipantMapper participantMapper;

    @Autowired
    CommonMapper commonMapper;

    @RequestMapping("/participant/information")
    public String Information(){

        return "creatinformation";
    }

    @RequestMapping("/participant/congress")
    public String lookCongress(Model model,HttpSession session){
        int commonId = commonMapper.getCommonIdByUsername((String)session.getAttribute("loginUser"));
        List<CongressVO> allCongresses = participantMapper.getCongressByCommonId(commonId);
        model.addAttribute("allCongress",allCongresses);

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

        model.addAttribute("congress",congress);
        model.addAttribute("organizerName",organizerName);
        model.addAttribute("formatter",formatter);
        model.addAttribute("record",record);
        model.addAttribute("participantInformation",participantInformation);

        //        判断当前时间用户是否可以参加会议
        if(now.isBefore(congress.getRegisterEndTime())&&now.isAfter(congress.getRegisterStartTime())){
            model.addAttribute("canRegisterCongress","yes");
        }
        return "/common/congress";
    }

}
