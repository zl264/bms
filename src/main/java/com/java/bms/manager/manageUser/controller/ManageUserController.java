package com.java.bms.manager.manageUser.controller;

import com.java.bms.manager.VO.UserVo;
import com.java.bms.manager.manageUser.mapper.ManageUserMapping;
import com.java.bms.manager.mapper.ManagerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public String deleteCommonUser(@RequestParam("commonId") int commonId, HttpSession session, Model model){
        manageUserMapping.deleteCommonUserLogin(commonId);
        manageUserMapping.deleteCommonUser(commonId);
        List<UserVo> allUser = manageUserMapping.getAllUser();
        model.addAttribute("allUser",allUser);
//        System.out.println(1);
        return "/manager/displayCommonUser";
    }

//    /**
//     * 修改制定的用户
//     */
//    @RequestMapping("/manageUser/update")
//    public String updateCommonUser(@RequestParam("id") int commonId, @RequestParam("username") String username, @RequestParam("name") String name, @RequestParam("sex") String sex, @RequestParam("idCardNo") String idCardNo,
//                                   @RequestParam("identity") String identity, @RequestParam("age") int age, @RequestParam("tel") String tel, Map<String, Object> map, HttpSession session){
//
//    }

}
