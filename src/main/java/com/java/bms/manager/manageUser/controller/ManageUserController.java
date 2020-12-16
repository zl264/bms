package com.java.bms.manager.manageUser.controller;

import com.java.bms.driver.VO.DriverVO;
import com.java.bms.manager.VO.UserVo;
import com.java.bms.manager.manageUser.mapper.ManageUserMapping;
import com.java.bms.manager.mapper.ManagerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ManageUserController {

    @Autowired
    ManagerMapper managerMapper;

    @Autowired
    ManageUserMapping manageUserMapping;

//    /*
//    显示参会人员信息
//    @return 参会人员信息
//     */
//    @RequestMapping(value = "/manageUser/display")
//    public String displayCommonUser(){
//        return "/manager/managerUser/displayCommonUser";
//    }

    /**
     * 显示所有用户信息
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/manageUser/display")
    public String displayCommonUser(HttpSession session, Model model){
        List<UserVo> allUser = manageUserMapping.getAllUser();

        model.addAttribute("allUser",allUser);
        return "/manager/displayCommonUser";
    }

    /**
     * 删除指定的用户
     */
    @RequestMapping("/manageUser/delete")
    public String deleteCommonUser(@RequestParam("id") int commonId, @RequestParam("username") String username,HttpSession session, Model model){
        manageUserMapping.deleteCommonUserLogin(commonId, username);
        manageUserMapping.deleteCommonUser(commonId, username);
        return "/manager/displayCommonUser";
    }


}
