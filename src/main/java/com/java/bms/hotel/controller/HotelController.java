package com.java.bms.hotel.controller;

import com.java.bms.common.VO.CommonUserVO;
import com.java.bms.hotel.VO.HotelCancelNote;
import com.java.bms.hotel.VO.HotelNoteVO;
import com.java.bms.hotel.VO.HotelVO;
import com.java.bms.hotel.mapper.HotelMapper;
import com.java.bms.other.DO.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * 对酒店用户登录注册的控制
 */
@Controller
public class HotelController {

    @Autowired
    HotelMapper hotelMapper;

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
                              @RequestParam("password") String password,
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
            return "/hotel/hotelLogin";
        } else{
            map.put("msg","出现错误，注册失败，请再次尝试或联系管理员");
            return "/hotel/hotelRegister";
        }
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
                note.getCheckInStartTime(),note.getCheckInEndTime(),note.getCommonName());
        List<HotelNoteVO> hotelOrderNotes = hotelMapper.getHotelOrderNoteByHotelId(hotelId);
        HotelVO hotel = hotelMapper.getHotelByHotelId(hotelId);
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
        hotelMapper.insertHotelOrderFailNote(hotelId,note.getCommonId(),hotel.getHotelPhone(),1,note.getTime(),
                note.getCheckInStartTime(),note.getCheckInEndTime());
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
        hotelMapper.deleteHotelCheckInNote(hotelId,commonId);
        List<HotelNoteVO> hotelOrderNotes = hotelMapper.getHotelOrderNoteByHotelId(hotelId);
        model.addAttribute("hotelOrderNotes",hotelOrderNotes);
        model.addAttribute("hotel",hotel);
        return "/hotel/orderNote";
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
     * 进入产看取消预约界面
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

    @RequestMapping("/hotel/relatedCongress")
    public String relatedCongress(HttpSession session,Model model){
        int hotelId = hotelMapper.getHotelIdByHotelUsername((String) session.getAttribute("hotelUsername"));


        HotelVO hotel = hotelMapper.getHotelByHotelId(hotelId);

        model.addAttribute("hotel",hotel);
        return "/hotel/relatedCongress";
    }

}
