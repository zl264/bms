package com.java.bms.common.controller;


import com.java.bms.other.DO.UserDO;
import com.java.bms.common.mapper.CommonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 对普通用户登录注册的控制
 */
@Controller
public class CommonController {

    @Autowired
    CommonMapper commonMapper;


    /**
     * 对普通用户的登录进行控制
     * @param username 用户名
     * @param password 密码
     * @param map 存储msg信息
     * @param session session
     * @return
     */
    @PostMapping(value = "/common/login")
    public String commonLogin(@RequestParam("username") String username,
                              @RequestParam("password") String password,
                              Map<String,Object> map, HttpSession session){
        if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
            map.put("msg","请输入用户名密码");
            return "index";
        }
        UserDO userDo = commonMapper.commonLogin(username,password);
        if(userDo==null){
            map.put("msg","用户名密码错误");
            return "index";
        }
        if(username.equals(userDo.getUsername())&&password.equals(userDo.getPasword())) {
//            登录成功以后，防止表单重复提交，可以重定向到主页
            session.setAttribute("loginUser", username);
            return "redirect:/commonMain";
        }
        return "index";
    }

    /**
     * 对普通用户的注册进行控制
     * @param username 用户名
     * @param password 密码
     * @param map 存储msg信息
     * @param session session
     * @return
     */
    @PostMapping(value = "/common/register")
    public String commonRegister(@RequestParam("username") String username,
                                 @RequestParam("password") String password,
                                 Map<String,Object> map, HttpSession session){
        if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
            map.put("msg","请输入要注册的用户名密码");
            return "register";
        }
        if(commonMapper.isRegister(username)!=null){
            map.put("msg","该用户名已经被注册了");
            return "register";
        }
        int result = commonMapper.commonRegister(username,password);
        if(result==1){
            map.put("msg","注册成功，请登录");
            return "index";
        } else{
            map.put("msg","出现错误，注册失败，请再次尝试或联系管理员");
            return "register";
        }
    }


}
