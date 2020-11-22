package com.java.bms.controller;


import com.java.bms.bean.User;
import com.java.bms.mapper.CommonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    CommonMapper commonMapper;

    @PostMapping(value = "/common/login")
    public String commonLogin(@RequestParam("username") String username,
                              @RequestParam("password") String password,
                              Map<String,Object> map, HttpSession session){
//        System.out.println("common+"+username+":"+password);
        if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
            System.out.println(1);
            map.put("msg","请输入用户名密码");
            return "index";
        }
//        System.out.println(123);
        User user = commonMapper.commonLogin(username,password);
        if(username.equals(user.getUsername())&&password.equals(user.getPasword())){
//            登录成功以后，防止表单重复提交，可以重定向到主页
            session.setAttribute("loginUser",username);
//            System.out.println("登录成功!");
            return "redirect:/commonMain";
        } else{
            map.put("msg","用户名密码错误");
            return "index";
        }
    }

    @RequestMapping("/hello")
    @ResponseBody
    public String hello(){
        return "hello";
    }
}
