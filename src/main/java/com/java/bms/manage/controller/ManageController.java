package com.java.bms.manage.controller;

import com.java.bms.manage.mapper.ManageMapper;
import com.java.bms.other.DO.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Map;


/**
 * 对管理员登录的控制
 */
@Controller
public class ManageController {
    @Autowired
    ManageMapper manageMapper;


    /**
     * 对管理员的登录进行控制
     * @param username 用户名
     * @param password 密码
     * @param map 存储msg信息
     * @param session session
     * @return
     */
    @PostMapping(value = "/manage/login")
    public String commonLogin(@RequestParam("username") String username,
                              @RequestParam("password") String password,
                              Map<String,Object> map, HttpSession session){
        if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
            map.put("msg","请输入用户名密码");
            return "index";
        }
        UserDO userDo = manageMapper.commonLogin(username,password);
        if(userDo==null){
            map.put("msg","用户名密码错误");
            return "index";
        }
        if(username.equals(userDo.getUsername())&&password.equals(userDo.getPasword())) {
//            登录成功以后，防止表单重复提交，可以重定向到主页
            session.setAttribute("loginUser", username);
            return "redirect:/manageMain";
        }
        return "index";
    }

}
