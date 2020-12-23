package com.java.bms.common.organizer.controller;

import com.java.bms.common.DO.ArrivalPlaceCountDO;
import com.java.bms.common.DO.CongressNoteVO;
import com.java.bms.common.VO.*;
import com.java.bms.common.mapper.CommonMapper;
import com.java.bms.common.organizer.mapper.CongressHotelMapper;
import com.java.bms.common.organizer.mapper.OrganizerMapper;
import com.java.bms.common.participant.mapper.ParticipantMapper;
import com.java.bms.driver.VO.DriverVO;
import com.java.bms.driver.mapper.DriverMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.expression.Strings;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
public class OrganizerController {

    @Autowired
    OrganizerMapper organizerMapper;

    @Autowired
    CommonMapper commonMapper;

    @Autowired
    DriverMapper driverMapper;

    @Autowired
    ParticipantMapper participantMapper;

    @Autowired
    CongressHotelMapper congressHotelMapper;
    /**
     * 跳转到创建会议界面
     * @return 创建会议界面
     */
    @RequestMapping("/organizer/creat")
    public String goCreatCongress(){
        return "/common/organizer/manageCongress";
    }


    /**
     * 创建会议并回到创建会议界面
     * @param title 会议标题
     * @param content 会议内容
     * @param place 会议地点
     * @param startTimeStr
     * @param endTimeStr
     * @param registerStartTimeStr
     * @param registerEndTimeStr
     * @param model 传递所有用户创建的会议
     * @param session session
     * @return 回到创建会议界面
     */
    @RequestMapping("/organizer/congress")
    public String createCongress(@RequestParam("title") String title,@RequestParam("content") String content,
                                 @RequestParam("place") String place,@RequestParam("startTime") String startTimeStr,
                                 @RequestParam("endTime") String endTimeStr, @RequestParam("registerStartTime") String registerStartTimeStr,
                                 @RequestParam("registerEndTime") String registerEndTimeStr,
                                 Model model,HttpSession session){

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime = LocalDateTime.parse(startTimeStr.replaceAll("T", " ") + ":00", df);
        LocalDateTime endTime = LocalDateTime.parse(endTimeStr.replaceAll("T", " ") + ":00", df);
        LocalDateTime registerStartTime = LocalDateTime.parse(registerStartTimeStr.replaceAll("T", " ") + ":00", df);
        LocalDateTime registerEndTime = LocalDateTime.parse(registerEndTimeStr.replaceAll("T", " ") + ":00", df);



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
        int commonId = commonMapper.getCommonIdByUsername((String)session.getAttribute("loginUser"));
        CommonUserVO user = commonMapper.getCommonUserByCommonId(commonId);
        model.addAttribute("allCongress",allCongress);
        model.addAttribute("user",user);
        return "/common/organizer/manageCongress";
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
    @RequestMapping("/organizer/alter")
    public String alterCongress(@RequestParam("title") String title,@RequestParam("content") String content,
                                @RequestParam("place") String place,@RequestParam("congressId") int congressId,
                                @RequestParam("startTime") String startTimeStr, @RequestParam("endTime") String endTimeStr,
                                @RequestParam("registerStartTime") String registerStartTimeStr,
                                @RequestParam("registerEndTime") String registerEndTimeStr,
                                Model model,HttpSession session, Map<String,Integer> map){
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
        List<DriverVO> hasDriver = organizerMapper.getDriverByCongressId2(congressId);

        model.addAttribute("allDriver",allDriver);
        model.addAttribute("applyDriver",applyDriver);
        model.addAttribute("hasDriver",hasDriver);
        model.addAttribute("congressId",congressId);
        return "/common/congress";
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
                            HttpSession session,Model model,Map<Object,Integer> map){
//        System.out.println(driverId);
//        System.out.println(congressId);
        organizerMapper.applyDriver(congressId,driverId);
        LocalDateTime now = LocalDateTime.now();

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


        return "/common/congress";
    }



    @RequestMapping("/organizer/delete/participant")
    public String deleteParticipant(@RequestParam("commonId") int commonId, @RequestParam("congressId") int congressId,
                                    Model model, HttpSession session, Map<String,Integer> map){
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
        List<CommonUserAllInformationVO> allInformationParticipants = commonMapper.getAllInformationParticipantIdByCongressId(congressId);
        //获取每个到达地点的人数
        List<ArrivalPlaceCountDO> allArrivalPlace = commonMapper.getAllParticipantPlaceByCongressId(congressId);
        //获取司机
        List<CongressHaveDriverVO> hasDriver = organizerMapper.getDriverByCongressId(congressId);
        for(CongressHaveDriverVO driver:hasDriver){
            map.put(String.valueOf(driver.getDriverId()),organizerMapper.getDriverListNum(driver.getDriverId(),congressId));
        }
        //判断当前参与者是否有填写完个人信息
        CommonUserVO participantInformation = commonMapper.HaveInfomation((String) session.getAttribute("loginUser"));
        //获取参与者的司机
        DriverUserVO participantDriver = participantMapper.getDriverByCongressIdAndCommonId(congressId,commonId);
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
        model.addAttribute("congress", congress);
        model.addAttribute("organizerName", organizerName);
        model.addAttribute("formatter", formatter);
        model.addAttribute("record", record);
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

    /**
     * 分配人员
     * @param request
     */
    @RequestMapping("/organizer/allocation")
    @ResponseBody
    public void allocationDriver(HttpServletRequest request){
        int congressId = Integer.parseInt(request.getParameter("congressId"));
        //空闲司机列表
        List<DriverVO> leisureDriver = new ArrayList<>();
        //获取会议已有的司机
        List<DriverVO> drivers = organizerMapper.getCongressDriverByCongressId(congressId);

        //获取哪些地方还要接送，并且那个地方还有多少人需要接送
        List<ArrivalPlaceCountDO> places = organizerMapper.getRemainderParticipant(congressId);

        for(DriverVO driverVO : drivers){
            //number是司机已经分配的人员数量
            int number = organizerMapper.getNumberByDriverIdAndCongressId(driverVO.getDriverId(),congressId);
            String driverPlace = organizerMapper.getPlaceByDriverIdAndCongressId(driverVO.getDriverId(),congressId);

            if(StringUtils.isEmpty(driverPlace)){
                //这个司机还未获得要接送的地点
                leisureDriver.add(driverVO);
            }
            if(!StringUtils.isEmpty(driverPlace)&&!StringUtils.isEmpty(places)){

                for(int x = 0;x<places.size();x++){
                    ArrivalPlaceCountDO place = places.get(x);
                    if(place.getArrivalPlace().equals(driverPlace)){
                        //num
                        int num = 0;
                        List<Integer> commonIds =
                                organizerMapper.getUnassignedCommonIdByArrivalPlace(place.getArrivalPlace(),congressId);
                        while(number<driverVO.getCapacity()&&num<place.getNum()){
                            //给用户分配司机
                            organizerMapper.participantAssignedDriver(commonIds.get(num),congressId,
                                    driverVO.getDriverId());
                            num++;
                            number++;
                        }
                        //这个接送地方的人员分配完毕
                        if(num==place.getNum()){
                            places.remove(place);
                            x--;
                        }
                    }
                }
            }
        }
//      给还未分配完的接送地点的名单从大到小排序
        Collections.sort(places,(o1,o2)->o2.getNum()-o1.getNum());
//      给未分配接送地点的司机按容量从大到小排序
        Collections.sort(leisureDriver,(o1,o2)->o2.getCapacity()-o1.getCapacity());
        places.forEach(System.out::println);
        leisureDriver.forEach(System.out::println);

        //给并未有分配过接送地点的司机分配人员
        while(leisureDriver.size()!=0&&places.size()!=0){
            for(int i=0,j=0;i<places.size()&&j<leisureDriver.size();i++,j++){
                DriverVO driver = leisureDriver.get(i);
                ArrivalPlaceCountDO place = places.get(i);

                //num
                int num = 0,number = 0;
                List<Integer> commonIds =
                        organizerMapper.getUnassignedCommonIdByArrivalPlace(place.getArrivalPlace(),congressId);
                while(number<driver.getCapacity()&&num<place.getNum()){
                    //给用户分配司机
                    organizerMapper.participantAssignedDriver(commonIds.get(num),congressId,
                            driver.getDriverId());
                    organizerMapper.setPinkUpPlace(congressId,driver.getDriverId(),place.getArrivalPlace());
                    num++;
                    number++;
                }
                //这个接送地方的人员分配完毕
                if(num==place.getNum()){
                    places.remove(place);
                    i--;
                }else{
                    place.setNum(place.getNum()-num);
                }
                //移除已经分配的司机
                leisureDriver.remove(driver);
                j--;
            }
        }

    }

    /**
     * 查看司机的接送名单
     * @param congressId
     * @param driverId
     * @param model
     * @return
     */
    @RequestMapping("/organizer/driver/list/{congressId}/{driverId}")
    public String driverNeedList(@PathVariable("congressId") int congressId,@PathVariable("driverId") int driverId,
                                 Model model){
        List<CommonUserAllInformationVO> userList = organizerMapper.getList(congressId,driverId);
        model.addAttribute("list",userList);
        model.addAttribute("congressId",congressId);
        return "/common/organizer/list";
    }




}
