package com.java.bms.driver.controller;

import com.java.bms.common.VO.CommonUserVO;
import com.java.bms.common.mapper.CommonMapper;
import com.java.bms.driver.DO.CongressApplyDriverDO;
import com.java.bms.driver.DO.UserDriverVO;
import com.java.bms.driver.VO.CongressDriver;
import com.java.bms.driver.VO.DriverVO;
import com.java.bms.driver.mapper.DriverMapper;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


/**
 * 对司机用户登录注册的控制
 */
@Controller
public class DriverController {
    @Autowired
    DriverMapper driverMapper;

    @Autowired
    CommonMapper commonMapper;

    /**
     * 主页进入司机登录界面
     * @return
     */
    @RequestMapping("/driver/enter")
    public String driverEnter(){
        return "/driver/driverLogin";
    }

    /**
     * 进入司机注册界面
     * @return
     */
    @RequestMapping("/driver/enterRegister")
    public String driverEnterRegister(){
        return "/driver/driverRegister";
    }


    /**
     * 对司机用户的登录进行控制
     * @param username 用户名
     * @param password 密码
     * @param map 存储msg信息
     * @param session session
     * @return
     */
    @PostMapping(value = "/driver/login")
    public String driverLogin(@RequestParam("username") String username,
                              @RequestParam("password") String password,
                              Map<String,Object> map, HttpSession session, Model model){
        if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
            session.setAttribute("msg","请输入用户名密码");
            return "redirect:/driver/enter";
        }
        UserDO userDo = driverMapper.driverLogin(username,password);
        if(userDo==null){
            map.put("msg","用户名密码错误");
            session.setAttribute("msg","用户名密码错误");
            return "redirect:/driver/enter";
        }
        if(username.equals(userDo.getUsername())&&password.equals(userDo.getPassword())) {
//            登录成功以后，防止表单重复提交，可以重定向到主页
            DriverVO driverVO = driverMapper.getDriverByDriverId(userDo.getId());
            List<CongressApplyDriverDO> applyCongresses = driverMapper.getApplyCongressesByDriverId(userDo.getId());
            List<CongressDriver> congresses = driverMapper.getCongressByDriverId(userDo.getId());

            session.removeAttribute("msg");
            session.setAttribute("loginUser", username);
            session.setAttribute("driver",driverVO);
            session.setAttribute("applyCongresses",applyCongresses);
            session.setAttribute("congresses",congresses);
            return "redirect:/driverMain";
        }
        return "/driver/driverLogin";
    }


    @PostMapping(value = "/driver/register")
    public String driverRegister(@RequestParam("username") String username,
                                 @RequestParam("password") String password,
                                 Map<String,Object> map, HttpSession session){
        if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
            map.put("msg","请输入要注册的用户名密码");
            return "/driver/driverRegister";
        }
        if(driverMapper.isRegister(username)!=null){
            map.put("msg","该用户名已经被注册了");
            return "/driver/driverRegister";
        }
        int result = driverMapper.driverRegister(username,password);
        if(result==1){
            map.put("msg","注册成功，请登录");
            return "/driver/driverLogin";
        } else{
            map.put("msg","出现错误，注册失败，请再次尝试或联系管理员");
            return "/driver/driverRegister";
        }
    }



    @RequestMapping(value = "/driver/information")
    public String DriverInformation(Map<String, Object> map, HttpSession session) throws UnsupportedEncodingException {
        int driverId = driverMapper.getDriverIdByUsername((String) session.getAttribute("loginUser"));
        DriverVO driver = driverMapper.getDriverByDriverId(driverId);
        map.put("driver", driver);
        return "/driver/main";
    }

    @RequestMapping("/driver/addinformation")
    public String addDriverInformation(@RequestParam("name") String name,@RequestParam("age") int age,
                                          @RequestParam("idCardNo") String idCardNo,@RequestParam("licensePlateNumber") String licensePlateNumber,
                                          @RequestParam("sex") String sex,@RequestParam("tel") String tel,
                                          @RequestParam("capacity") int capacity,Map<String,Object>map,
                                          HttpSession session){
        String username = (String)session.getAttribute("loginUser");
        int driverId = driverMapper.getDriverIdByUsername(username);
        DriverVO driver = driverMapper.getDriverByDriverId(driverId);
        List<CongressApplyDriverDO> applyCongresses = driverMapper.getApplyCongressesByDriverId(driverId);
        List<CongressDriver> congresses = driverMapper.getCongressByDriverId(driverId);
        session.setAttribute("driver",driver);
        session.setAttribute("applyCongresses",applyCongresses);
        session.setAttribute("congresses",congresses);
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(sex) || StringUtils.isEmpty(age) || StringUtils.isEmpty(capacity)
                || StringUtils.isEmpty(idCardNo) || StringUtils.isEmpty(licensePlateNumber) || StringUtils.isEmpty(tel)) {
            map.put("msg", "请填写完整信息");
            return "/driver/main";
        }
        if(driver==null){
            int result=driverMapper.addDriverInformation(driverId,username,name,tel,capacity,licensePlateNumber,sex,age,idCardNo);
            driver = driverMapper.getDriverByDriverId(driverId);
            map.put("driver", driver);
            if (result == 1) {
                map.put("msg", "填写或修改信息成功！");
                return "/driver/main";
            } else {
                map.put("msg", "出现错误，修改信息失败，请再次尝试或联系管理员");
                return "/driver/main";
            }
        }else{
            int result =  driverMapper.updateDriverInformation(driverId,username,name,tel,capacity,licensePlateNumber,sex,age,idCardNo);
            driver = driverMapper.getDriverByDriverId(driverId);
            map.put("driver", driver);
            if (result == 1) {
                map.put("msg", "填写或修改信息成功！");
                return "/driver/main";
            } else {
                map.put("msg", "出现错误，修改信息失败，请再次尝试或联系管理员");
                return "/driver/main";
            }
        }
    }

/*    @RequestMapping("/driver/alter")
    public String alterDriverInformation(@RequestParam("name") String name,@RequestParam("age") int age,
                                       @RequestParam("idCardNo") String idCardNo,@RequestParam("licensePlateNumber") String licensePlateNumber,
                                       @RequestParam("sex") String sex,@RequestParam("tel") String tel,
                                       @RequestParam("capacity") int capacity,
                                       HttpSession session){
        String username = (String)session.getAttribute("loginUser");
        int driverId = commonMapper.getCommonIdByUsername(username);
        driverMapper.updateDriverInformation(driverId,username,name,tel,capacity,licensePlateNumber,sex,age,idCardNo);
        DriverVO driverVO = driverMapper.getDriverByDriverId(driverId);
        List<CongressApplyDriverDO> applyCongresses = driverMapper.getApplyCongressesByDriverId(driverId);
        List<CongressDriver> congresses = driverMapper.getCongressByDriverId(driverId);

        session.setAttribute("driver",driverVO);
        session.setAttribute("applyCongresses",applyCongresses);
        session.setAttribute("congresses",congresses);
        return "redirect:/driverMain";
    }*/


    @RequestMapping("/driver/agree")
    public String agreeCongress(@RequestParam("year") int year,@RequestParam("month") int month,
                                @RequestParam("day") int day,@RequestParam("hour") int hour,
                                @RequestParam("minute") int minute,@RequestParam("place") String place,
                                @RequestParam("congressId") int congressId,HttpSession session){
        LocalDateTime time = LocalDateTime.of(year,month,day,hour,minute);
        int driverId = commonMapper.getCommonIdByUsername((String)session.getAttribute("loginUser"));
        driverMapper.deleteApplyCongressByDriverIdAndCongressId(driverId,congressId);
        driverMapper.addCongressDriver(driverId,congressId);
        DriverVO driverVO = driverMapper.getDriverByDriverId(driverId);
        List<CongressApplyDriverDO> applyCongresses = driverMapper.getApplyCongressesByDriverId(driverId);
        List<CongressDriver> congresses = driverMapper.getCongressByDriverId(driverId);

        session.setAttribute("driver",driverVO);
        session.setAttribute("applyCongresses",applyCongresses);
        session.setAttribute("congresses",congresses);
        return "redirect:/driverMain";
    }

    @RequestMapping("/driver/refuse/{congressId}")
    public String refuseCongress(@PathVariable("congressId") int congressId,HttpSession session){
        int driverId = commonMapper.getCommonIdByUsername((String)session.getAttribute("loginUser"));
        driverMapper.deleteApplyCongressByDriverIdAndCongressId(driverId,congressId);
        driverMapper.addDriverRefuseCongress(driverId,congressId);
        DriverVO driverVO = driverMapper.getDriverByDriverId(driverId);
        List<CongressApplyDriverDO> applyCongresses = driverMapper.getApplyCongressesByDriverId(driverId);
        List<CongressDriver> congresses = driverMapper.getCongressByDriverId(driverId);

        session.setAttribute("driver",driverVO);
        session.setAttribute("applyCongresses",applyCongresses);
        session.setAttribute("congresses",congresses);
        return "redirect:/driverMain";
    }

    /**
     * 司机完成接送任务
     * @param congressId
     * @param session
     * @return
     */
    @RequestMapping("/driver/complete/{congressId}")
    public String taskComplete(@PathVariable("congressId") int congressId,HttpSession session){
        int driverId = commonMapper.getCommonIdByUsername((String)session.getAttribute("loginUser"));
        driverMapper.deleteCongressDriver(driverId,congressId);

        DriverVO driverVO = driverMapper.getDriverByDriverId(driverId);
        List<CongressApplyDriverDO> applyCongresses = driverMapper.getApplyCongressesByDriverId(driverId);
        List<CongressDriver> congresses = driverMapper.getCongressByDriverId(driverId);

        session.setAttribute("driver",driverVO);
        session.setAttribute("applyCongresses",applyCongresses);
        session.setAttribute("congresses",congresses);
        return "redirect:/driverMain";
    }

    @RequestMapping("/driver/list/{congressId}")
    public String getList(@PathVariable("congressId") int congressId,HttpSession session,Model model){
        int driverId = commonMapper.getCommonIdByUsername((String)session.getAttribute("loginUser"));
        List<UserDriverVO> list = driverMapper.getList(driverId,congressId);
        model.addAttribute("list",list);
        return "/driver/list";
    }

}
