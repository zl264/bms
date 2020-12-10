package com.java.bms.manager.controller;

import com.java.bms.manager.mapper.ManagerMapper;
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
 * 对管理员登录的控制
 */
@Controller
public class ManagerController {
    @Autowired
    ManagerMapper manageMapper;

    /**
     * 主页进入管理员登录界面
     * @return
     */
    @RequestMapping("/manager/enter")
    public String managerEnter(){
        return "/manager/managerLogin";
    }

    /**
     * 对管理员的登录进行控制
     * @param username 用户名
     * @param password 密码
     * @param map 存储msg信息
     * @param session session
     * @return
     */
    @PostMapping(value = "/manager/login")
    public String commonLogin(@RequestParam("username") String username,
                              @RequestParam("password") String password,
                              Map<String,Object> map, HttpSession session, Model model){
        if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
            session.setAttribute("msg","请输入用户名密码");
            return "redirect:/manager/enter";
        }
        UserDO userDo = manageMapper.commonLogin(username,password);
        if(userDo==null){
            session.setAttribute("msg","用户名密码错误");
            return "redirect:/manager/enter";
        }
        if(username.equals(userDo.getUsername())&&password.equals(userDo.getPassword())) {
//            登录成功以后，防止表单重复提交，可以重定向到主页
            session.setAttribute("loginUser", username);
            session.removeAttribute("msg");
            return "redirect:/managerMain";
        }
        return "/manager/managerLogin";
    }

}
