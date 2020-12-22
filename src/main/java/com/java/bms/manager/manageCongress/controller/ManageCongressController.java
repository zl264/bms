package com.java.bms.manager.manageCongress.controller;

import com.java.bms.manager.manageCongress.mapper.ManageCongressMapping;
import com.java.bms.manager.manageUser.mapper.ManageUserMapping;
import com.java.bms.manager.mapper.ManagerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.java.bms.common.VO.*;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ManageCongressController {

    @Autowired
    ManagerMapper managerMapper;

    @Autowired
    ManageCongressMapping managerCongressMapper;

    @RequestMapping("/manageCongress/display")
    public String returnMain(HttpSession session){
        List<CongressVO> allCongress = managerCongressMapper.getAllCongress();
//        int commonId = commonMapper.getCommonIdByUsername((String)session.getAttribute("loginUser"));
//        CommonUserVO user = commonMapper.getCommonUserByCommonId(commonId);
        session.setAttribute("allCongress", allCongress);
//        session.setAttribute("user",user);
        return "/manager/main";
    }

}
