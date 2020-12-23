package com.java.bms.manager.controller;

import com.java.bms.driver.DO.CongressApplyDriverDO;
import com.java.bms.driver.VO.CongressDriver;
import com.java.bms.driver.VO.DriverVO;
import com.java.bms.driver.mapper.DriverMapper;
import com.java.bms.manager.mapper.ManagerDriverMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@Controller
public class ManagerDriverController {

    @Autowired
    ManagerDriverMapper managerDriverMapper;

    @Autowired
    DriverMapper driverMapper;

    /**
     * 进入查看司机界面
     * @param model
     * @return
     */
    @RequestMapping("/manager/allDriver")
    public String managerDriver(Model model){
        List<DriverVO> allDriver = managerDriverMapper.getAllDriver();

        model.addAttribute("allDriver",allDriver);
        return "/manager/allDriver";
    }

    /**
     * 删除司机
     * @param driverId
     * @param model
     * @return
     */
    @RequestMapping("/manager/deleteDriver/{driverId}")
    public String deleteDriver(@PathVariable("driverId") int driverId, Model model){
        managerDriverMapper.deleteDriver(driverId);
        managerDriverMapper.deleteCongressApplyDriverByDriverId(driverId);
        managerDriverMapper.deleteCongressDriverByDriverId(driverId);
        managerDriverMapper.deleteDriverLogin(driverId);
        managerDriverMapper.deleteDriverRefuseCongressByDriverId(driverId);
        managerDriverMapper.deleteUserDriverByDriverId(driverId);

        List<DriverVO> allDriver = managerDriverMapper.getAllDriver();

        model.addAttribute("allDriver",allDriver);
        return "/manager/allDriver";
    }


    /**
     * 进入司机信息界面
     * @param map
     * @param session
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/manager/updateDriver/{driverId}")
    public String DriverInformation(@PathVariable("driverId") int driverId,
                                    Map<String, Object> map, HttpSession session) {

        DriverVO driver = driverMapper.getDriverByDriverId(driverId);
        map.put("driver", driver);
        session.setAttribute("driver",driver);
        return "/manager/updateDriver";
    }


    /**
     * 修改或者添加司机信息
     * @param name
     * @param age
     * @param idCardNo
     * @param licensePlateNumber
     * @param sex
     * @param tel
     * @param capacity
     * @param session
     * @return
     */
    @RequestMapping("/manager/addinformation")
    public String addDriverInformation(@RequestParam("name") String name,@RequestParam("age") int age,
                                       @RequestParam("idCardNo") String idCardNo,@RequestParam("licensePlateNumber") String licensePlateNumber,
                                       @RequestParam("sex") String sex,@RequestParam("tel") String tel,
                                       @RequestParam("capacity") int capacity,@RequestParam("driverId") int driverId,
                                       HttpSession session){
        DriverVO driver = driverMapper.getDriverByDriverId(driverId);
        List<CongressApplyDriverDO> applyCongresses = driverMapper.getApplyCongressesByDriverId(driverId);
        List<CongressDriver> congresses = driverMapper.getCongressByDriverId(driverId);
        session.setAttribute("driver",driver);
        session.setAttribute("applyCongresses",applyCongresses);
        session.setAttribute("congresses",congresses);
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(sex) || StringUtils.isEmpty(age) || StringUtils.isEmpty(capacity)
                || StringUtils.isEmpty(idCardNo) || StringUtils.isEmpty(licensePlateNumber) || StringUtils.isEmpty(tel)) {
            session.setAttribute("driverMsg", "请填写完整信息");
            return "redirect:/manager/updateDriver/"+driverId;
        }
        if(driver==null){
            int result=driverMapper.addDriverInformation(driverId,driver.getUsername(),name,tel,capacity,licensePlateNumber,sex,age,idCardNo);
            driver = driverMapper.getDriverByDriverId(driverId);
            session.setAttribute("driver", driver);
            if (result == 1) {
                session.setAttribute("driverMsg", "填写或修改信息成功！");
                return "redirect:/manager/updateDriver/"+driverId;
            } else {
                session.setAttribute("driverMsg", "出现错误，修改信息失败，请再次尝试或联系管理员");
                return "redirect:/manager/updateDriver/"+driverId;
            }
        }else{
            int result =  driverMapper.updateDriverInformation(driverId,driver.getUsername(),name,tel,capacity,licensePlateNumber,sex,age,idCardNo);
            driver = driverMapper.getDriverByDriverId(driverId);
            session.setAttribute("driver", driver);
            if (result == 1) {
                session.setAttribute("driverMsg", "填写或修改信息成功！");
                return "redirect:/manager/updateDriver/"+driverId;
            } else {
                session.setAttribute("driverMsg", "出现错误，修改信息失败，请再次尝试或联系管理员");
                return "redirect:/manager/updateDriver/"+driverId;
            }
        }
    }
}
