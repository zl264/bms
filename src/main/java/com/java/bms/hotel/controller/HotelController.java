package com.java.bms.hotel.controller;

import com.java.bms.common.VO.CommonUserVO;
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
            session.setAttribute("loginUser", username);
            session.removeAttribute("msg");
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
    public String hotelInformation(){ return "/hotel/hotelInformation"; }
    /**
     * 进入客房查询界面
     */
    @RequestMapping("/hotel/searchRoom")
    public String searchRoom(){ return "/hotel/searchRoom"; }
    /**
     * 进入预定记录界面
     */
    @RequestMapping("/hotel/orderNote")
    public String orderNote(){ return "/hotel/orderNote"; }
    /**
     * 进入预定成功界面
     */
    @RequestMapping("/hotel/checkInNote")
    public String checkInNote(){ return "/hotel/checkInNote"; }
    /**
     * 进入预定失败界面
     */
    @RequestMapping("/hotel/orderFailNote")
    public String orderFailNote(){ return "/hotel/orderFailNote"; }
    /**
     * 进入预定房间界面
     */
    @RequestMapping("/hotel/bookRoom")
    public String bookRoom(){ return "/hotel/bookRoom"; }
    /**
     * 继续预定
     */
    @RequestMapping("/hotel/lookHotel")
    public String lookHotel(){ return "/hotel/lookHotel"; }
    /**
     * 注销
     */
    @RequestMapping("/hotel/hotelLogin")
    public String hotelLogin(){ return "/hotel/hotelLogin"; }

    @RequestMapping("/hotel/main")
    public String main(){ return "/hotel/main"; }

    /**
     * 酒店注册信息输入
     *......
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
                                Map<String, Object> map, HttpSession session) {
        int hotelId = hotelMapper.getHotelIdByLoginId((String) session.getAttribute("loginHotel"));
        if (StringUtils.isEmpty(hotelName)||StringUtils.isEmpty(hotelPhone) || StringUtils.isEmpty(hotelLocation) || StringUtils.isEmpty(singleRoomPrice)||StringUtils.isEmpty(remainDoubleRoom)||StringUtils.isEmpty(hotelDescription)
                || StringUtils.isEmpty(doubleRoomPrice) || StringUtils.isEmpty(totalSingleRoom) || StringUtils.isEmpty(remainSingleRoom)||StringUtils.isEmpty(totalDoubleRoom)) {
            map.put("msg", "请填写完整信息");
            return "/hotel/main";
        }
        if(hotelMapper.isRegisterHotel(hotelName) !=null){
            int result = hotelMapper.createHotel((String)session.getAttribute("loginHotel"), hotelId,hotelName,hotelPhone,hotelLocation,singleRoomPrice,doubleRoomPrice,totalSingleRoom,remainSingleRoom,totalDoubleRoom,remainDoubleRoom,hotelDescription);
            HotelVO user = hotelMapper.HaveHotel(hotelMapper.getHotelNameById(hotelId));
            map.put("user", user);
            if (result == 1) {
                map.put("msg", "填写或修改信息成功！");
                return "/hotel/main";
            } else {
                map.put("msg", "出现错误，修改信息失败，请再次尝试或联系管理员");
                return "/hotel/main";
            }
        }else{
            int result = hotelMapper.updateHotel((String)session.getAttribute("loginHotel"), hotelId,hotelName,hotelPhone,hotelLocation,singleRoomPrice,doubleRoomPrice,totalSingleRoom,remainSingleRoom,totalDoubleRoom,remainDoubleRoom,hotelDescription);
            HotelVO user = hotelMapper.HaveHotel(hotelMapper.getHotelNameById(hotelId));
            map.put("user", user);
            if (result == 1) {
                map.put("msg", "填写或修改信息成功！");
                return "/hotel/main";
            } else {
                map.put("msg", "出现错误，修改信息失败，请再次尝试或联系管理员");
                return "/hotel/main";
            }
        }
    }

    @RequestMapping(value = "/hotel/ordernote")
    public void hotelOrderNote(@RequestParam("commonId") int commonId,
                               @RequestParam("commonPhone") int commonPhone,
                               Model model, HttpSession session){
        int hotelId = hotelMapper.getHotelOrderById((String) session.getAttribute("loginUser"));
        HotelVO visit = hotelMapper.HaveHotel(hotelMapper.getHotelNameById(hotelId));
    }
}
