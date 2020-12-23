package com.java.bms.hotel.controller;

import com.java.bms.common.VO.CommonUserVO;
import com.java.bms.common.VO.CongressVO;
import com.java.bms.common.mapper.CommonMapper;
import com.java.bms.hotel.VO.HotelCongressVO;
import com.java.bms.hotel.VO.HotelCancelNote;
import com.java.bms.hotel.VO.HotelNoteVO;
import com.java.bms.hotel.VO.HotelVO;
import com.java.bms.hotel.mapper.HotelMapper;
import com.java.bms.other.DO.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * 对酒店用户登录注册的控制
 */
@Controller
public class HotelController {

    @Autowired
    HotelMapper hotelMapper;

    @Autowired
    CommonMapper commonMapper;

    /**
     * 进入酒店用户登录界面
     * @return
     */
    @RequestMapping("/hotel/enter")
    public String hotelEnter(){
        return "/hotel/hotelLogin";
    }

    /**
     * 进入酒店用户注册界面
     * @return
     */
    @RequestMapping("/hotel/enterRegister")
    public String hotelEnterRegister(){
        return "/hotel/hotelRegister";
    }

    /**
     * 对酒店用户的登录进行控制
     * @param username 用户名
     * @param password 密码
     * @param map 存储msg信息
     * @param session session
     * @return
     */
    @PostMapping(value = "/hotel/login")
    public String hotelLogin(@RequestParam("username") String username,
                              @RequestParam("password") String password,@RequestParam("code") String code,
                              Map<String,Object> map, HttpSession session, Model model){
        if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
            session.setAttribute("msg","请输入用户名密码");
            return "redirect:/hotel/enter";
        }
        UserDO userDo = hotelMapper.commonLogin(username,password);
        if(userDo==null){
            session.setAttribute("msg","用户名密码错误");
            return "redirect:/hotel/enter";
        }
        if(StringUtils.isEmpty(code)){
            session.setAttribute("msg","请输入验证码");
            return "redirect:/common/enter";
        }
        if (!code.equals(session.getAttribute("VerifyCode"))){
            session.setAttribute("msg","验证码错误");
            return "redirect:/common/enter";
        }
        if(username.equals(userDo.getUsername())&&password.equals(userDo.getPassword())) {
//            登录成功以后，防止表单重复提交，可以重定向到主页
            session.setAttribute("hotelUsername", username);
            session.removeAttribute("msg");
            int hotelId = hotelMapper.getHotelIdByHotelUsername(username);
            HotelVO hotel =  hotelMapper.getHotelByHotelId(hotelId);
            session.setAttribute("hotel",hotel);
            return "redirect:/hotelMain";
        }
        return "/hotel/hotelLogin";
    }

    /**
     * 对酒店用户的注册进行控制
     * @param username 用户名
     * @param password 密码
     * @param map 存储msg信息
     * @param session session
     * @return
     */
    @PostMapping(value = "/hotel/register")
    public String hotelRegister(@RequestParam("username") String username,
                                 @RequestParam("password") String password,
                                 Map<String,Object> map, HttpSession session){
        if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
            map.put("msg","请输入要注册的用户名密码");
            return "/hotel/hotelRegister";
        }
        if(hotelMapper.isRegister(username)!=null){
            map.put("msg","该用户名已经被注册了");
            return "/hotel/hotelRegister";
        }
        int result = hotelMapper.commonRegister(username,password);
        if(result==1){
            map.put("msg","注册成功，请登录");
            session.setAttribute("msg","注册成功，请登录");
            return "/hotel/hotelLogin";
        } else{
            map.put("msg","出现错误，注册失败，请再次尝试或联系管理员");
            return "/hotel/hotelRegister";
        }
    }


    /**
     * 进入用户忘记密码界面
     * @return
     */
    @RequestMapping("/hotel/loss")
    public String enterLossPassword(){
        return "/hotel/lossPassword";
    }

    /**
     * 判断用户输入的用户名和手机号是否一致
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/hotel/equal")
    public String hotelLossPassword(HttpServletRequest request, HttpSession session, Model model){
        String username = (String)request.getParameter("username");
        String tel = (String) request.getParameter("tel");

        if(hotelMapper.usernameAndTelIsRight(username,tel)!=0){
            model.addAttribute("switch",1);
            int hotelId = hotelMapper.getHotelIdByHotelName(username);
            model.addAttribute("hotelId",hotelId);
        }else{
            model.addAttribute("msg","用户名或手机号输入错误");
        }

        return "/hotel/lossPassword";
    }

    @RequestMapping("/hotel/updatePassword")
    public String hotelUpdatePassword(HttpSession session,HttpServletRequest request){
        int hotelId = Integer.parseInt(request.getParameter("hotelId"));
        String password = request.getParameter("password");
        hotelMapper.updatePassword(hotelId,password);

        session.setAttribute("msg","修改密码成功");
        return "/hotel/hotelLogin";
    }


    /**
     * 进入酒店信息界面
     */
    @RequestMapping("/hotel/hotelInformation")
    public String hotelInformation(Model model,HttpSession session){
        int hotelId = hotelMapper.getHotelIdByHotelUsername((String)session.getAttribute("hotelUsername"));
        HotelVO hotel = hotelMapper.getHotelByHotelId(hotelId);
        model.addAttribute("hotel",hotel);
        return "/hotel/hotelInformation";
    }



    /**
     * 进入预约记录界面
     */
    @RequestMapping("/hotel/orderNote")
    public String orderNote(HttpSession session,Model model){
        String hotelUsername = (String)session.getAttribute("hotelUsername");
        int hotelId = hotelMapper.getHotelIdByHotelUsername(hotelUsername);
        List<HotelNoteVO> hotelOrderNotes = hotelMapper.getHotelOrderNoteByHotelId(hotelId);
        HotelVO hotel = hotelMapper.getHotelByHotelId(hotelId);
        model.addAttribute("hotelOrderNotes",hotelOrderNotes);
        model.addAttribute("hotel",hotel);
        return "/hotel/orderNote";
    }

    /**
     * 同意预约
     * @param hotelId
     * @param commonId
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/hotel/agree")
    public String agreeOrder(@RequestParam("hotelId") int hotelId,@RequestParam("commonId") int commonId,
                             HttpSession session,Model model){
        HotelNoteVO note = hotelMapper.getHotelOrderNoteByHotelIdAndCommonId(hotelId,commonId);
        hotelMapper.deleteHotelOrderNoteByHotelIdAndCommonId(hotelId,commonId);
        hotelMapper.insertHotelCheckInNote(note.getHotelId(),note.getCommonId(),note.getCommonPhone(),note.getTime(),
                note.getCheckInStartTime(),note.getCheckInEndTime(),note.getCommonName(),note.getType());
        HotelVO hotel = hotelMapper.getHotelByHotelId(hotelId);
        if(note.getType()==1){
            if(hotel.getRemainSingleRoom()>0){
                hotelMapper.updateSingleRoom(hotelId,hotel.getRemainSingleRoom()-1);
            }
        }else{
            if(hotel.getRemainDoubleRoom()>0){
                hotelMapper.updateDoubleRoom(hotelId,hotel.getRemainDoubleRoom()-1);
            }
        }

        List<HotelNoteVO> hotelOrderNotes = hotelMapper.getHotelOrderNoteByHotelId(hotelId);
        hotel = hotelMapper.getHotelByHotelId(hotelId);
        model.addAttribute("hotelOrderNotes",hotelOrderNotes);
        model.addAttribute("hotel",hotel);
        return "/hotel/orderNote";
    }

    /**
     * 拒绝预约
     * @param hotelId
     * @param commonId
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/hotel/refuse")
    public String refuseOrder(@RequestParam("hotelId") int hotelId,@RequestParam("commonId") int commonId,
                              HttpSession session,Model model){
        HotelNoteVO note = hotelMapper.getHotelOrderNoteByHotelIdAndCommonId(hotelId,commonId);
        HotelVO hotel = hotelMapper.getHotelByHotelId(hotelId);
        hotelMapper.deleteHotelOrderNoteByHotelIdAndCommonId(hotelId,commonId);
//        hotelMapper.insertHotelOrderFailNote(hotelId,note.getCommonId(),hotel.getHotelPhone(),1,note.getTime(),
//                note.getCheckInStartTime(),note.getCheckInEndTime());

        List<HotelNoteVO> hotelOrderNotes = hotelMapper.getHotelOrderNoteByHotelId(hotelId);
        model.addAttribute("hotelOrderNotes",hotelOrderNotes);
        model.addAttribute("hotel",hotel);
        return "/hotel/orderNote";
    }

    /**
     * 进入预约成功界面
     */
    @RequestMapping("/hotel/checkInNote")
    public String checkInNote(HttpSession session,Model model){
        String hotelUsername = (String)session.getAttribute("hotelUsername");
        int hotelId = hotelMapper.getHotelIdByHotelUsername(hotelUsername);
        List<HotelNoteVO> hotelCheckInNotes = hotelMapper.getHotelCheckInNoteByHotelId(hotelId);
        HotelVO hotel = hotelMapper.getHotelByHotelId(hotelId);
        model.addAttribute("hotelCheckInNotes",hotelCheckInNotes);
        model.addAttribute("hotel",hotel);
        return "/hotel/checkInNote";
    }

    /**
     * 退房,删除预约成功记录
     * @param hotelId
     * @param commonId
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/hotel/checkout")
    public String checkout(@RequestParam("hotelId") int hotelId,@RequestParam("commonId") int commonId,
                           HttpSession session,Model model){

        HotelVO hotel = hotelMapper.getHotelByHotelId(hotelId);
        HotelNoteVO hotelNote = hotelMapper.getSingleHotelCheckInNote(hotelId,commonId);
        hotelMapper.deleteHotelCheckInNote(hotelId,commonId);
        if(hotelNote.getType()==1){
            hotelMapper.updateSingleRoom(hotelId,hotel.getRemainSingleRoom()+1);
        }else{
            hotelMapper.updateDoubleRoom(hotelId,hotel.getRemainDoubleRoom()+1);
        }
        hotel = hotelMapper.getHotelByHotelId(hotelId);
        List<HotelNoteVO> hotelOrderNotes = hotelMapper.getHotelOrderNoteByHotelId(hotelId);
        model.addAttribute("hotelOrderNotes",hotelOrderNotes);
        model.addAttribute("hotel",hotel);
        return "/hotel/checkInNote";
    }


    /**
     * 注销
     */
    @RequestMapping("/hotel/hotelLogin")
    public String hotelLogin(){ return "/hotel/hotelLogin"; }

    @RequestMapping("/hotel/main")
    public String main(){ return "/hotel/main"; }


    /**
     * 对酒店的信息进行注册
     * @param hotelName
     * @param hotelPhone
     * @param hotelLocation
     * @param singleRoomPrice
     * @param doubleRoomPrice
     * @param totalSingleRoom
     * @param remainSingleRoom
     * @param totalDoubleRoom
     * @param remainDoubleRoom
     * @param hotelDescription
     * @param model
     * @param session
     * @return
     */
    @RequestMapping(value = "/hotel/createHotel")
    public String createHotel(@RequestParam("hotelName") String hotelName,
                                @RequestParam("hotelPhone") String hotelPhone,
                                @RequestParam("hotelLocation") String hotelLocation,
                                @RequestParam("singleRoomPrice") int singleRoomPrice,
                                @RequestParam("doubleRoomPrice") int doubleRoomPrice,
                                @RequestParam("totalSingleRoom") int totalSingleRoom,
                                @RequestParam("remainSingleRoom") int remainSingleRoom,
                                @RequestParam("totalDoubleRoom") int totalDoubleRoom,
                                @RequestParam("remainDoubleRoom") int remainDoubleRoom,
                                @RequestParam("hotelDescription") String hotelDescription,
                                Model model, HttpSession session) {
        int hotelId = hotelMapper.getHotelIdByHotelUsername((String) session.getAttribute("hotelUsername"));
        hotelMapper.createHotel(hotelName,hotelId,hotelPhone,hotelLocation,singleRoomPrice,doubleRoomPrice,
                totalSingleRoom,remainSingleRoom,totalDoubleRoom,remainDoubleRoom,hotelDescription);
        HotelVO hotel = hotelMapper.getHotelByHotelId(hotelId);
        model.addAttribute("hotel",hotel);
        return "/hotel/hotelInformation";
    }

    /**
     * 修改酒店信息
     * @param hotelName
     * @param hotelPhone
     * @param hotelLocation
     * @param singleRoomPrice
     * @param doubleRoomPrice
     * @param totalSingleRoom
     * @param remainSingleRoom
     * @param totalDoubleRoom
     * @param remainDoubleRoom
     * @param hotelDescription
     * @param model
     * @param session
     * @return
     */
    @RequestMapping("/hotel/updateHotel")
    public String updateHotel(@RequestParam("hotelName") String hotelName,
                              @RequestParam("hotelPhone") String hotelPhone,
                              @RequestParam("hotelLocation") String hotelLocation,
                              @RequestParam("singleRoomPrice") int singleRoomPrice,
                              @RequestParam("doubleRoomPrice") int doubleRoomPrice,
                              @RequestParam("totalSingleRoom") int totalSingleRoom,
                              @RequestParam("remainSingleRoom") int remainSingleRoom,
                              @RequestParam("totalDoubleRoom") int totalDoubleRoom,
                              @RequestParam("remainDoubleRoom") int remainDoubleRoom,
                              @RequestParam("hotelDescription") String hotelDescription,
                              Model model, HttpSession session){
        int hotelId = hotelMapper.getHotelIdByHotelUsername((String) session.getAttribute("hotelUsername"));
        hotelMapper.updateHotel(hotelName,hotelId,hotelPhone,hotelLocation,singleRoomPrice,doubleRoomPrice,
                totalSingleRoom,remainSingleRoom,totalDoubleRoom,remainDoubleRoom,hotelDescription);
        HotelVO hotel = hotelMapper.getHotelByHotelId(hotelId);
        model.addAttribute("hotel",hotel);
        return "/hotel/hotelInformation";
    }


    /**
     * 进入查看取消预约界面
     * @param model
     * @param session
     * @return
     */
    @RequestMapping("/hotel/cancelNote")
    public String cancelNote(Model model, HttpSession session){
        int hotelId = hotelMapper.getHotelIdByHotelUsername((String) session.getAttribute("hotelUsername"));
        List<HotelCancelNote> cancelNotes = hotelMapper.getHotelCancelOrderByHotelId(hotelId);
        HotelVO hotel = hotelMapper.getHotelByHotelId(hotelId);
        model.addAttribute("cancelNotes",cancelNotes);
        model.addAttribute("hotel",hotel);
        return "/hotel/cancelNote";
    }


    /**
     * 确认用户取消记录信息
     * @param hotelId
     * @param commonId
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/hotel/cancel")
    public String confirmCancelNote(@RequestParam("hotelId") int hotelId,@RequestParam("commonId") int commonId,
                                    HttpSession session,Model model){
        hotelMapper.deleteHotelCancelOrderByHotelIdAndCommonId(hotelId,commonId);
        List<HotelCancelNote> cancelNotes = hotelMapper.getHotelCancelOrderByHotelId(hotelId);
        HotelVO hotel = hotelMapper.getHotelByHotelId(hotelId);
        model.addAttribute("cancelNotes",cancelNotes);
        model.addAttribute("hotel",hotel);
        return "/hotel/cancelNote";
    }

    /**
     * 进入相关酒店界面
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/hotel/relatedCongress")
    public String relatedCongress(HttpSession session,Model model){
        int hotelId = hotelMapper.getHotelIdByHotelUsername((String) session.getAttribute("hotelUsername"));

        List<HotelCongressVO> applyCongresses = hotelMapper.getApplyCongress(hotelId);
        List<HotelCongressVO> haveCongresses = hotelMapper.getHaveCongress(hotelId);
        HotelVO hotel = hotelMapper.getHotelByHotelId(hotelId);

        model.addAttribute("hotel",hotel);
        model.addAttribute("applyCongresses",applyCongresses);
        model.addAttribute("haveCongresses",haveCongresses);
        return "/hotel/relatedCongress";
    }

    /**
     * 同意申请的会议
     * @param hotelId
     * @param congressId
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/hotel/agreeCongress")
    public String agreeCongress(@RequestParam("hotelId") int hotelId, @RequestParam("congressId") int congressId,
                                HttpSession session,Model model){
        hotelMapper.deleteApplyCongressNote(hotelId,congressId);
        hotelMapper.insertHaveCongressNote(hotelId,congressId);

        List<HotelCongressVO> applyCongresses = hotelMapper.getApplyCongress(hotelId);
        List<HotelCongressVO> haveCongresses = hotelMapper.getHaveCongress(hotelId);
        HotelVO hotel = hotelMapper.getHotelByHotelId(hotelId);

        model.addAttribute("hotel",hotel);
        model.addAttribute("applyCongresses",applyCongresses);
        model.addAttribute("haveCongresses",haveCongresses);
        return "/hotel/relatedCongress";
    }

    /**
     * 拒绝申请的会议
     * @param hotelId
     * @param congressId
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/hotel/refuseCongress")
    public String refuseCongress(@RequestParam("hotelId") int hotelId, @RequestParam("congressId") int congressId,
                                HttpSession session,Model model){
        hotelMapper.deleteApplyCongressNote(hotelId,congressId);

        List<HotelCongressVO> applyCongresses = hotelMapper.getApplyCongress(hotelId);
        List<HotelCongressVO> haveCongresses = hotelMapper.getHaveCongress(hotelId);
        HotelVO hotel = hotelMapper.getHotelByHotelId(hotelId);

        model.addAttribute("hotel",hotel);
        model.addAttribute("applyCongresses",applyCongresses);
        model.addAttribute("haveCongresses",haveCongresses);
        return "/hotel/relatedCongress";
    }


    /**
     * 删除已有的会议
     * @param hotelId
     * @param congressId
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/hotel/deleteCongress")
    public String deleteCongress(@RequestParam("hotelId") int hotelId, @RequestParam("congressId") int congressId,
                                HttpSession session,Model model){
        hotelMapper.deleteHaveCongressNote(hotelId,congressId);

        List<HotelCongressVO> applyCongresses = hotelMapper.getApplyCongress(hotelId);
        List<HotelCongressVO> haveCongresses = hotelMapper.getHaveCongress(hotelId);
        HotelVO hotel = hotelMapper.getHotelByHotelId(hotelId);

        model.addAttribute("hotel",hotel);
        model.addAttribute("applyCongresses",applyCongresses);
        model.addAttribute("haveCongresses",haveCongresses);
        return "/hotel/relatedCongress";
    }

    /**
     * 进入酒店页面
     * @param hotelId
     * @param commonId
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/hotel/show")
    public String showHotel(@RequestParam("hotelId") int hotelId,@RequestParam("commonId") int commonId,
                            HttpSession session,Model model){
        HotelVO hotel = hotelMapper.getHotelByHotelId(hotelId);
        int isOrder = hotelMapper.isOrderHotel(hotelId,commonId);
        int isCheckIn = hotelMapper.isCheckInHotel(hotelId,commonId);
        int isFree = hotelMapper.isFree(hotelId,commonId);
        long day = 0;
        HotelNoteVO note = null;
        if(isOrder==1){
            note = hotelMapper.orderHotelNote(hotelId,commonId);
            day = Duration.between(note.getCheckInStartTime(),note.getCheckInEndTime()).toDays();
        }
        if(isCheckIn==1){
            note = hotelMapper.checkInHotel(hotelId,commonId);
            day = Duration.between(note.getCheckInStartTime(),note.getCheckInEndTime()).toDays();
        }


        model.addAttribute("hotel",hotel);
        model.addAttribute("isOrder",isOrder);
        model.addAttribute("isCheckIn",isCheckIn);
        model.addAttribute("isFree",isFree);
        model.addAttribute("commonId",commonId);
        model.addAttribute("note",note);
        model.addAttribute("day",day);
        return "/hotel/showHotel";
    }

    /**
     * 用户预约酒店
     * @param hotelId
     * @param commonId
     * @param type
     * @param checkInStartTimeStr
     * @param dayNumber
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/hotel/commonApply")
    public String commonApply(@RequestParam("hotelId") int hotelId,@RequestParam("commonId") int commonId,
                              @RequestParam("type") int type, @RequestParam("checkInStartTime") String checkInStartTimeStr,
                              @RequestParam("dayNumber") int dayNumber, HttpSession session,Model model){
        HotelVO hotel = hotelMapper.getHotelByHotelId(hotelId);
        CommonUserVO commonUserVO = commonMapper.getCommonUserByCommonId(commonId);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime checkInStartTime = LocalDateTime.parse(checkInStartTimeStr.replaceAll("T", " ") + ":00", df);
        LocalDateTime checkInEndTime = checkInStartTime.plusDays(dayNumber);

        if(type==1){
            if(hotel.getRemainSingleRoom()>0){
                hotelMapper.insertOrder(hotelId,commonId,commonUserVO.getTel(),now,checkInStartTime,checkInEndTime,
                        commonUserVO.getName(),1);
//                hotelMapper.updateSingleRoom(hotelId,hotel.getRemainSingleRoom()-1);
            }
        }else{
            if(hotel.getRemainDoubleRoom()>0){
                hotelMapper.insertOrder(hotelId,commonId,commonUserVO.getTel(),now,checkInStartTime,checkInEndTime,
                        commonUserVO.getName(),2);
//                hotelMapper.updateDoubleRoom(hotelId,hotel.getRemainDoubleRoom()-1);
            }
        }
        hotel = hotelMapper.getHotelByHotelId(hotelId);
        int isOrder = hotelMapper.isOrderHotel(hotelId,commonId);
        int isCheckIn = hotelMapper.isCheckInHotel(hotelId,commonId);
        int isFree = hotelMapper.isFree(hotelId,commonId);
        long day = 0;
        HotelNoteVO note = null;
        if(isOrder==1){
            note = hotelMapper.orderHotelNote(hotelId,commonId);
            day = Duration.between(note.getCheckInStartTime(),note.getCheckInEndTime()).toDays();
        }
        if(isCheckIn==1){
            note = hotelMapper.checkInHotel(hotelId,commonId);
            day = Duration.between(note.getCheckInStartTime(),note.getCheckInEndTime()).toDays();
        }


        model.addAttribute("hotel",hotel);
        model.addAttribute("isOrder",isOrder);
        model.addAttribute("isCheckIn",isCheckIn);
        model.addAttribute("isFree",isFree);
        model.addAttribute("commonId",commonId);
        model.addAttribute("note",note);
        model.addAttribute("day",day);
        return "/hotel/showHotel";
    }

    /**
     * 提供给酒店查看的会议信息
     * @param congressId 会议ID
     * @param model
     * @return
     */
    @RequestMapping("/hotel/lookCongress/{congressId}")
    public String hotelLookCongress(@PathVariable("congressId") int congressId, Model model){
        CongressVO congress = commonMapper.getCongressById(congressId);
        String organizerName = commonMapper.getUsernameById((int) congress.getOrganizerId());
        model.addAttribute("congress",congress);
        model.addAttribute("organizerName",organizerName);
        return "/common/hotelLookCongress";
    }

    /**
     * 普通用户取消预约(取消申请预约和取消预约成功的记录)
     * @param commonId
     * @param hotelId
     * @param applyType
     * @param model
     * @return
     */
    @RequestMapping("/hotel/commonCancelHotel")
    public String commonCancelHotel(@RequestParam("commonId") int commonId,@RequestParam("hotelId") int hotelId,
                                    @RequestParam("applyType") int applyType,Model model){
        if(applyType==1){
            hotelMapper.deleteHotelOrderNoteByHotelIdAndCommonId(hotelId,commonId);
        }
        if(applyType==2){
            HotelNoteVO note = hotelMapper.checkInHotel(hotelId,commonId);
            hotelMapper.deleteHotelCheckInNote(hotelId,commonId);
            if(hotelMapper.isHaveCancelNote(hotelId,commonId)==0){
                hotelMapper.insertHotelCancelNote(hotelId,commonId,note.getCommonPhone(),note.getCommonName());
            }
        }

        HotelVO hotel = hotelMapper.getHotelByHotelId(hotelId);
        int isOrder = hotelMapper.isOrderHotel(hotelId,commonId);
        int isCheckIn = hotelMapper.isCheckInHotel(hotelId,commonId);
        int isFree = hotelMapper.isFree(hotelId,commonId);
        long day = 0;
        HotelNoteVO note = null;
        if(isOrder==1){
            note = hotelMapper.orderHotelNote(hotelId,commonId);
            day = Duration.between(note.getCheckInStartTime(),note.getCheckInEndTime()).toDays();
        }
        if(isCheckIn==1){
            note = hotelMapper.checkInHotel(hotelId,commonId);
            day = Duration.between(note.getCheckInStartTime(),note.getCheckInEndTime()).toDays();
        }


        model.addAttribute("hotel",hotel);
        model.addAttribute("isOrder",isOrder);
        model.addAttribute("isCheckIn",isCheckIn);
        model.addAttribute("isFree",isFree);
        model.addAttribute("commonId",commonId);
        model.addAttribute("note",note);
        model.addAttribute("day",day);
        return "/hotel/showHotel";
    }



}
