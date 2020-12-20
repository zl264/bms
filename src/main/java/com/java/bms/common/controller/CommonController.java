package com.java.bms.common.controller;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.java.bms.common.DO.ArrivalPlaceCountDO;
import com.java.bms.common.DO.CongressNoteVO;
import com.java.bms.common.VO.*;
import com.java.bms.common.organizer.mapper.CongressHotelMapper;
import com.java.bms.common.organizer.mapper.OrganizerMapper;
import com.java.bms.common.participant.mapper.ParticipantMapper;
import com.java.bms.driver.VO.DriverVO;
import com.java.bms.hotel.VO.HotelVO;
import com.java.bms.other.DO.UserDO;
import com.java.bms.common.mapper.CommonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对普通用户登录注册的控制
 */
@Controller
public class CommonController {

    @Autowired
    CommonMapper commonMapper;

    @Autowired
    OrganizerMapper organizerMapper;

    @Autowired
    ParticipantMapper participantMapper;

    @Autowired
    CongressHotelMapper congressHotelMapper;


    /**
     * 主页点击连接进入普通用户登录界面
     *
     * @return
     */
    @RequestMapping("/common/enter")
    public String commonEnter() {
        return "/common/commonLogin";
    }

    /**
     * 进入普通用户注册界面
     *
     * @return
     */
    @RequestMapping("/common/enterRegister")
    public String commonEnterRegister() {
        return "/common/commonRegister";
    }


    /**
     * 对普通用户的登录进行控制
     *
     * @param username 用户名
     * @param password 密码
     * @param map      存储msg信息
     * @param session  session
     * @return
     */
    @PostMapping(value = "/common/login")
    public String commonLogin(@RequestParam("username") String username,
                              @RequestParam("password") String password,/*@RequestParam("code") String code,*/
                              Map<String, Object> map, HttpSession session, Model model) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            session.setAttribute("msg", "请输入用户名密码");
            return "redirect:/common/enter";
        }
        UserDO userDo = commonMapper.commonLogin(username, password);
        if (userDo == null) {
//            map.put("msg","用户名密码错误");
            session.setAttribute("msg", "用户名密码错误");
            return "redirect:/common/enter";
        }
//        if(StringUtils.isEmpty(code)){
//            session.setAttribute("msg","请输入验证码");
//            return "redirect:/common/enter";
//        }
//        if (!code.equals(session.getAttribute("VerifyCode"))){
//            session.setAttribute("msg","验证码错误");
//            return "redirect:/common/enter";
//        }
        if (username.equals(userDo.getUsername()) && password.equals(userDo.getPassword())) {
//            登录成功以后，防止表单重复提交，可以重定向到主页
            session.setAttribute("loginUser", username);
            List<CongressVO> allCongress = commonMapper.getAllCongress();
            session.setAttribute("allCongress", allCongress);
            session.removeAttribute("msg");
            return "redirect:/commonMain";
        }
        return "/common/commonLogin";
    }

    /**
     * 对普通用户的注册进行控制
     *
     * @param username 用户名
     * @param password 密码
     * @param map      存储msg信息
     * @param session  session
     * @return
     */
    @RequestMapping(value = "/common/register")
    public String commonRegister(@RequestParam("username") String username,
                                 @RequestParam("password") String password,
                                 Map<String, Object> map, HttpSession session) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            map.put("msg", "请输入要注册的用户名密码");
            return "/common/commonRegister";
        }
        if (commonMapper.isRegister(username) != null) {
            map.put("msg", "该用户名已经被注册了");
            return "/common/commonRegister";
        }
        int result = commonMapper.commonRegister(username, password);
        if (result == 1) {
            map.put("msg", "注册成功，请登录");
            return "/common/commonLogin";
        } else {
            map.put("msg", "出现错误，注册失败，请再次尝试或联系管理员");
            return "/common/commonRegister";
        }
    }

    /**
     * 新的用户注册
     * @param request
     * @param session
     * @param map
     * @return
     */
    @RequestMapping("/common/register1")
    public String commonRegister1(HttpServletRequest request,HttpSession session,Map<Object,String> map){
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String sex = request.getParameter("sex");
        String idCardNo = request.getParameter("idCardNo");
        String identity = request.getParameter("identity");
        int age = Integer.parseInt(request.getParameter("age"));
        String tel = request.getParameter("tel");
        if (commonMapper.isRegister(username) != null) {
            map.put("msg", "该用户名已经被注册了");
            return "/common/commonRegister";
        }
        int result = commonMapper.commonRegister(username, password);
        if (result == 1) {
            map.put("msg", "注册成功，请登录");
            int commonId = commonMapper.getCommonIdByUsername(username);
            commonMapper.createInformation(username,name,age,idCardNo,identity,sex,commonId,tel);
            return "/common/commonLogin";
        } else {
            map.put("msg", "出现错误，注册失败，请再次尝试或联系管理员");
            return "/common/commonRegister";
        }
    }

    @RequestMapping("/common/returnMain")
    public String returnMain(HttpSession session){
        List<CongressVO> allCongress = commonMapper.getAllCongress();
        session.setAttribute("allCongress", allCongress);
        return "/common/main";
    }

    /**
     * 通过会议ID查找会议
     *
     * @param id    会议ID
     * @param model 传递相关信息
     * @return 会议界面
     */
    @RequestMapping("/congress/{id}")
    public String getCongressByID(@PathVariable("id") Integer id, Model model, HttpSession session,
                                  Map<String,Integer> map) {
        int userId = commonMapper.getCommonIdByUsername((String) session.getAttribute("loginUser"));
        CongressVO congress = commonMapper.getCongressById(id);
        String organizerName = commonMapper.getUsernameById((int) congress.getOrganizerId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        CongressNoteVO record = commonMapper.getCongressNoteByCommonIdAndCongressId(userId, congress.getCongressId());
        LocalDateTime now = LocalDateTime.now();
        //获取所有参加会议的参加者
        List<CommonUserVO> participants = commonMapper.getParticipantIdByCongressId((int)congress.getCongressId());
        //获取所有填写了到达时间和到达地点的参加者
        List<CommonUserAllInformationVO> allInformationParticipants = commonMapper.getAllInformationParticipantIdByCongressId((int)(congress.getCongressId()));
        //判断当前参与者是否有填写完个人信息
        CommonUserVO participantInformation = commonMapper.HaveInfomation((String) session.getAttribute("loginUser"));
        //获取每个到达地点的人数
        List<ArrivalPlaceCountDO> allArrivalPlace = commonMapper.getAllParticipantPlaceByCongressId(id);
        //获取会议司机
        List<CongressHaveDriverVO> hasDriver = organizerMapper.getDriverByCongressId(id);
        for(CongressHaveDriverVO driver:hasDriver){
            map.put(String.valueOf(driver.getDriverId()),organizerMapper.getDriverListNum(driver.getDriverId(),id));
        }
        //获取参与者的司机
        DriverUserVO participantDriver = participantMapper.getDriverByCongressIdAndCommonId(id,userId);
        List<DriverVO> allDriver = organizerMapper.getAllDriver();
        List<DriverVO> applyDriver = organizerMapper.getApplyDriver(id);
        List<DriverVO> hasDriver1 = organizerMapper.getDriverByCongressId1(id);

        //得到所有的酒店
        List<CongressHotelVO> allHotel = congressHotelMapper.getAllHotel(id);
        //得到已申请的会议
        List<CongressHotelVO> applyHotel = congressHotelMapper.getCongressApplyHotel(id);
        //得到会议所有的酒店
        List<CongressHotelVO> hasHotel = congressHotelMapper.getCongressHotel(id);

        model.addAttribute("congress", congress);
        model.addAttribute("organizerName", organizerName);
        model.addAttribute("formatter", formatter);
        model.addAttribute("record", record);
        model.addAttribute("hasDriver",hasDriver);
        model.addAttribute("hasDriver1",hasDriver1);
        model.addAttribute("participants", participants);
        model.addAttribute("participantInformation",participantInformation);
        model.addAttribute("allInformationParticipants",allInformationParticipants);
        model.addAttribute("allArrivalPlace",allArrivalPlace);
        model.addAttribute("driverHaveNum",map);
        model.addAttribute("participantDriver",participantDriver);
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


    @RequestMapping("/common/search")
    public String searchCongress(@RequestParam("information") String information, Model model, HttpSession session) {
        List<CongressVO> searchCongress = commonMapper.searchCongressByInformation(information);
        List<HotelVO> searchHotel = commonMapper.searchHotelByInformation(information);
        int commonId = commonMapper.getCommonIdByUsername((String)session.getAttribute("loginUser"));
        model.addAttribute("searchCongress",searchCongress);
        model.addAttribute("searchHotel",searchHotel);
        model.addAttribute("commonId",commonId);
        return "/common/search";
    }


    /**
     * 对普通用户的信息进行控制
     *
     * @param map     信息
     * @param session session
     * @return
     */
    @RequestMapping(value = "/common/information")
    public String commonInformation(Map<String, Object> map, HttpSession session) throws UnsupportedEncodingException {
        int commonId = commonMapper.getCommonIdByUsername((String) session.getAttribute("loginUser"));
        CommonUserVO user = commonMapper.HaveInfomation(commonMapper.getUsernameById(commonId));
        map.put("user", user);
        return "/common/participant/information";
    }


    @RequestMapping(value = "/common/createinformation")
    public String createInformation( @RequestParam("name") String name,
                                     @RequestParam("age") int age, @RequestParam("idCardNo") String idCardNo,
                                     @RequestParam("identity") String identity, @RequestParam("sex") String sex, @RequestParam("tel") String tel,
                                     Map<String, Object> map, HttpSession session) {
        int commonId = commonMapper.getCommonIdByUsername((String) session.getAttribute("loginUser"));
        CommonUserVO visit = commonMapper.HaveInfomation(commonMapper.getUsernameById(commonId));
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(sex) || StringUtils.isEmpty(age)
                || StringUtils.isEmpty(idCardNo) || StringUtils.isEmpty(identity) || StringUtils.isEmpty(tel)) {
            map.put("msg", "请填写完整信息");
            return "/common/participant/information";
        }
        if(visit==null){
            int result = commonMapper.createInformation((String)session.getAttribute("loginUser"), name, age, idCardNo, identity, sex, commonId, tel);
            CommonUserVO user = commonMapper.HaveInfomation(commonMapper.getUsernameById(commonId));
            map.put("user", user);
            if (result == 1) {
                map.put("msg", "填写或修改信息成功！");
                return "/common/participant/information";
            } else {
                map.put("msg", "出现错误，修改信息失败，请再次尝试或联系管理员");
                return "/common/participant/information";
            }
        }else{
            int result = commonMapper.updateInformation((String)session.getAttribute("loginUser"), name, age, idCardNo, identity, sex, commonId, tel);
            CommonUserVO user = commonMapper.HaveInfomation(commonMapper.getUsernameById(commonId));
            map.put("user", user);
            if (result == 1) {
                map.put("msg", "填写或修改信息成功！");
                return "/common/participant/information";
            } else {
                map.put("msg", "出现错误，修改信息失败，请再次尝试或联系管理员");
                return "/common/participant/information";
            }
        }

    }


    /**
     * 提供给司机查看的会议信息
     * @param congressId 会议ID
     * @param model
     * @return
     */
    @RequestMapping("/other/congress/{congressId}")
    public String otherLookCongress(@PathVariable("congressId") int congressId,Model model){
        CongressVO congress = commonMapper.getCongressById(congressId);
        String organizerName = commonMapper.getUsernameById((int) congress.getOrganizerId());
        model.addAttribute("congress",congress);
        model.addAttribute("organizerName",organizerName);
        return "/common/otherLookCongress";
    }


}
