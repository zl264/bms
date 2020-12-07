package com.java.bms.driver.controller;

import com.java.bms.driver.mapper.DriverMapper;
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
 * 对司机用户登录注册的控制
 */
@Controller
public class DriverController {
    @Autowired
    DriverMapper driverMapper;

    /**
     * 主页进入司机登录界面
     * @return
     */
    @RequestMapping("/driver/enter")
    public String driverEnter(){
        return "/driver/driverLogin";
    }

    /**
     * 进入司机注册界面
     * @return
     */
    @RequestMapping("/driver/enterRegister")
    public String driverEnterRegister(){
        return "/driver/driverRegister";
    }


    /**
     * 对司机用户的登录进行控制
     * @param username 用户名
     * @param password 密码
     * @param map 存储msg信息
     * @param session session
     * @return
     */
    @PostMapping(value = "/driver/login")
    public String driverLogin(@RequestParam("username") String username,
                              @RequestParam("password") String password,
                              Map<String,Object> map, HttpSession session, Model model){
        if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
            session.setAttribute("msg","请输入用户名密码");
            return "redirect:/driver/driverLogin";
        }
        UserDO userDo = driverMapper.commonLogin(username,password);
        if(userDo==null){
            map.put("msg","用户名密码错误");
            session.setAttribute("msg","用户名密码错误");
            return "redirect:/driver/driverLogin";
        }
        if(username.equals(userDo.getUsername())&&password.equals(userDo.getPassword())) {
//            登录成功以后，防止表单重复提交，可以重定向到主页
            session.setAttribute("loginUser", username);
            return "redirect:/driverMain";
        }
        return "/driver/driverLogin";
    }

    /**
     * 对司机用户的注册进行控制
     * @param username 用户名
     * @param password 密码
     * @param map 存储msg信息
     * @param session session
     * @return
     */
    @PostMapping(value = "/driver/register")
    public String driverRegister(@RequestParam("username") String username,
                                 @RequestParam("password") String password,
                                 Map<String,Object> map, HttpSession session){
        if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
            map.put("msg","请输入要注册的用户名密码");
            return "/driver/driverRegister";
        }
        if(driverMapper.isRegister(username)!=null){
            map.put("msg","该用户名已经被注册了");
            return "/driver/driverRegister";
        }
        int result = driverMapper.commonRegister(username,password);
        if(result==1){
            map.put("msg","注册成功，请登录");
            return "/driver/driverLogin";
        } else{
            map.put("msg","出现错误，注册失败，请再次尝试或联系管理员");
            return "/driver/driverRegister";
        }
    }
}
