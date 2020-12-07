package com.java.bms.common.controller;


import com.java.bms.common.DO.CongressNoteVO;
import com.java.bms.common.VO.CommonUserVO;
import com.java.bms.common.VO.CongressVO;
import com.java.bms.other.DO.UserDO;
import com.java.bms.common.mapper.CommonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * 对普通用户登录注册的控制
 */
@Controller
public class CommonController {

    @Autowired
    CommonMapper commonMapper;

    /**
     * 主页点击连接进入普通用户登录界面
     * @return
     */
    @RequestMapping("/common/enter")
    public String commonEnter(){
        return "/common/commonLogin";
    }

    /**
     * 进入普通用户注册界面
     * @return
     */
    @RequestMapping("/common/enterRegister")
    public String commonEnterRegister(){
        return "/common/commonRegister";
    }


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
                              Map<String,Object> map, HttpSession session, Model model){
        if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
            session.setAttribute("msg","请输入用户名密码");
            return "redirect:/common/commonLogin";
        }
        UserDO userDo = commonMapper.commonLogin(username,password);
        if(userDo==null){
//            map.put("msg","用户名密码错误");
            session.setAttribute("msg","用户名密码错误");
            return "redirect:/common/commonLogin";
        }
        if(username.equals(userDo.getUsername())&&password.equals(userDo.getPassword())) {
//            登录成功以后，防止表单重复提交，可以重定向到主页
            session.setAttribute("loginUser", username);
            List<CongressVO> allCongress = commonMapper.getAllCongress();
            session.setAttribute("allCongress",allCongress);
            return "redirect:/commonMain";
        }
        return "/common/commonLogin";
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
            return "/common/commonRegister";
        }
        if(commonMapper.isRegister(username)!=null){
            map.put("msg","该用户名已经被注册了");
            return "/common/commonRegister";
        }
        int result = commonMapper.commonRegister(username,password);
        if(result==1){
            map.put("msg","注册成功，请登录");
            return "/common/commonLogin";
        } else{
            map.put("msg","出现错误，注册失败，请再次尝试或联系管理员");
            return "/common/commonRegister";
        }
    }

    /**
     * 通过会议ID查找会议
     * @param id 会议ID
     * @param model 传递相关信息
     * @return 会议界面
     */
    @RequestMapping("/congress/{id}")
    public String getCongressByID(@PathVariable("id") Integer id,Model model,HttpSession session){
        int userId = commonMapper.getCommonIdByUsername((String)session.getAttribute("loginUser"));
        CongressVO congress = commonMapper.getCongressById(id);
        String organizerName = commonMapper.getUsernameById((int)congress.getOrganizerId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        CongressNoteVO record = commonMapper.getCongressNoteByCommonIdAndCongressId(userId,congress.getCongressId());
        List<CommonUserVO> participants = commonMapper.getParticipantIdByCongressId(congress.getOrganizerId());


        model.addAttribute("congress",congress);
        model.addAttribute("organizerName",organizerName);
        model.addAttribute("formatter",formatter);
        model.addAttribute("record",record);
        model.addAttribute("participants",participants);
//        if(congress.getStartTime().isBefore(congress.getEndTime())) {
//
//            System.out.println(1);
//        }else{
//            System.out.println(0);
//        }
        return "/common/congress";
    }



    @RequestMapping("/common/search")
    public String searchCongress(@RequestParam("title") String title, Model model,HttpSession session){
        

        return "/common/search";
    }
}
