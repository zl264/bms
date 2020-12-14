package com.java.bms.hotel.controller;

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
            return "redirect:/hotel/enter";
        }
        if (!code.equals(session.getAttribute("VerifyCode"))){
            session.setAttribute("msg","验证码错误");
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
}
