package com.java.bms.common.controller;


import com.java.bms.other.DO.UserDO;
import com.java.bms.common.mapper.CommonMapper;
import com.java.bms.other.config.AllConfig;
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
        if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){

            map.put("msg","请输入用户名密码");
            return "index";
        }
        UserDO userDo = commonMapper.commonLogin(username,password);
        if(username.equals(userDo.getUsername())&&password.equals(userDo.getPasword())){
//            登录成功以后，防止表单重复提交，可以重定向到主页
            session.setAttribute("loginUser",username);
            return "redirect:/commonMain";
        } else{
            map.put("msg","用户名密码错误");
            return "index";
        }
    }

    @RequestMapping("/zenglin/zneglin")
    @ResponseBody
    public String hello(){
        return "hello";
    }

}
