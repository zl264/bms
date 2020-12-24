package com.java.bms.manager.controller;

import com.java.bms.common.VO.CommonUserVO;
import com.java.bms.common.mapper.CommonMapper;
import com.java.bms.manager.mapper.ManagerMapper;
import com.java.bms.other.DO.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;


/**
 * 对管理员登录的控制
 * 管理普通用户
 */
@Controller
public class ManagerController {
    @Autowired
    ManagerMapper manageMapper;

    @Autowired
    CommonMapper commonMapper;

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
                              @RequestParam("password") String password,@RequestParam("code") String code,
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
        if(StringUtils.isEmpty(code)){
            session.setAttribute("msg","请输入验证码");
            return "redirect:/common/enter";
        }
        if (!code.equals(session.getAttribute("VerifyCode"))){
            session.setAttribute("msg","验证码错误");
            return "redirect:/common/enter";
        }
        if(username.equals(userDo.getUsername())&&password.equals(userDo.getPassword())) {
//            登录成功以后，防止表单重复提交，可以重定向到主页
            List<CommonUserVO> allCommonUser = manageMapper.getAllCommonUser();

            session.setAttribute("loginUser", username);
            session.removeAttribute("msg");
            session.setAttribute("allCommonUser",allCommonUser);
            return "redirect:/managerMain";
        }
        return "/manager/managerLogin";
    }

    /**
     * 删除用户所有记录
     * @param commonId
     * @param session
     * @return
     */
    @RequestMapping("/manager/deleteCommon/{commonId}")
    public String deleteCommonUser(@PathVariable("commonId") int commonId, HttpSession session){
        manageMapper.deleteCommonUser(commonId);
        manageMapper.deleteCommonLogin(commonId);
        manageMapper.deleteCongressByOrganizerId(commonId);
        manageMapper.deleteCongressNoteByCommonId(commonId);
        manageMapper.deleteHotelCancelOrderByCommonId(commonId);
        manageMapper.deleteHotelCheckInNoteByCommonId(commonId);
        manageMapper.deleteHotelOrderNoteByCommonId(commonId);
        manageMapper.deleteUserDriverByCommonId(commonId);


        List<CommonUserVO> allCommonUser = manageMapper.getAllCommonUser();
        session.setAttribute("allCommonUser",allCommonUser);
        return "redirect:/managerMain";
    }

    /**
     * 返回管理员主界面
     * @param session
     * @return
     */
    @RequestMapping("/manager/returnMain")
    public String returnMain(HttpSession session){
        List<CommonUserVO> allCommonUser = manageMapper.getAllCommonUser();
        session.setAttribute("allCommonUser",allCommonUser);
        return "redirect:/managerMain";
    }


    /**
     * 对普通用户的信息进行控制
     * @param map     信息
     * @param session session
     * @return
     */
    @RequestMapping(value = "/manager/updateCommon/{commonId}")
    public String commonInformation(@PathVariable("commonId") int commonId,
                                    Map<String, Object> map, HttpSession session) {

        CommonUserVO user = commonMapper.HaveInfomation(commonMapper.getUsernameById(commonId));
        map.put("user", user);
        return "/manager/updateCommon";
    }

    /**
     * 修改用户信息
     * @param name
     * @param age
     * @param idCardNo
     * @param identity
     * @param sex
     * @param tel
     * @param map
     * @param session
     * @return
     */
    @RequestMapping(value = "/manager/createinformation")
    public String createInformation( @RequestParam("name") String name,@RequestParam("commonId") int commonId,
                                     @RequestParam("age") int age, @RequestParam("idCardNo") String idCardNo,
                                     @RequestParam("identity") String identity, @RequestParam("sex") String sex, @RequestParam("tel") String tel,
                                     Map<String, Object> map, HttpSession session,Model model) {

        CommonUserVO visit = commonMapper.HaveInfomation(commonMapper.getUsernameById(commonId));
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(sex) || StringUtils.isEmpty(age)
                || StringUtils.isEmpty(idCardNo) || StringUtils.isEmpty(identity) || StringUtils.isEmpty(tel)) {
            map.put("msg", "请填写完整信息");
            return "/manager/updateCommon";
        }
        if(visit==null){
            int result = commonMapper.createInformation(commonMapper.getUsernameById(commonId), name, age, idCardNo, identity, sex, commonId, tel);
            CommonUserVO user = commonMapper.HaveInfomation(commonMapper.getUsernameById(commonId));
            map.put("user", user);
            if (result == 1) {
                map.put("msg", "填写或修改信息成功！");
                return "/manager/updateCommon";
            } else {
                map.put("msg", "出现错误，修改信息失败，请再次尝试或联系管理员");
                return "/manager/updateCommon";
            }
        }else{
            int result = commonMapper.updateInformation(commonMapper.getUsernameById(commonId), name, age, idCardNo, identity, sex, commonId, tel);
            CommonUserVO user = commonMapper.HaveInfomation(commonMapper.getUsernameById(commonId));
            model.addAttribute("user",user);

            if (result == 1) {
                map.put("msg", "填写或修改信息成功！");
                return "/manager/updateCommon";
            } else {
                map.put("msg", "出现错误，修改信息失败，请再次尝试或联系管理员");
                return "/manager/updateCommon";
            }
        }

    }

}
