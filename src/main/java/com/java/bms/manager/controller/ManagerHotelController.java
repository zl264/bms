package com.java.bms.manager.controller;


import com.java.bms.hotel.VO.HotelVO;
import com.java.bms.hotel.mapper.HotelMapper;
import com.java.bms.manager.mapper.ManagerHotelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ManagerHotelController {


    @Autowired
    ManagerHotelMapper managerHotelMapper;

    @Autowired
    HotelMapper hotelMapper;

    /**
     * 进入查看酒店界面
     * @param model
     * @return
     */
    @RequestMapping("/manager/allHotel")
    public String managerAllHotel(Model model){
        List<HotelVO> allHotel = managerHotelMapper.getAllHotel();

        model.addAttribute("allHotel",allHotel);
        return "/manager/allHotel";
    }

    /**
     * 删除酒店相关记录
     * @param hotelId
     * @param model
     * @return
     */
    @RequestMapping("/manager/deleteHotel/{hotelId}")
    public String deleteHotel(@PathVariable("hotelId") int hotelId,Model model){
        managerHotelMapper.deleteHotel(hotelId);
        managerHotelMapper.deleteCongressApplyHotelByHotelId(hotelId);
        managerHotelMapper.deleteCongressHotelByHotelId(hotelId);
        managerHotelMapper.deleteHotelCancelOrderByHotelId(hotelId);
        managerHotelMapper.deleteHotelCheckInNoteByHotelId(hotelId);
        managerHotelMapper.deleteHotelLogin(hotelId);
        managerHotelMapper.deleteHotelOrderNote(hotelId);
        managerHotelMapper.deleteOrderFailNote(hotelId);

        List<HotelVO> allHotel = managerHotelMapper.getAllHotel();

        model.addAttribute("allHotel",allHotel);
        return "/manager/allHotel";
    }


    /**
     * 查看酒店信息
     * @param hotelId
     * @param model
     * @return
     */
    @RequestMapping("/manager/lookHotel/{hotelId}")
    public String lookHotel(@PathVariable("hotelId") int hotelId,Model model){
        HotelVO hotel = hotelMapper.getHotelByHotelId(hotelId);

        model.addAttribute("hotel",hotel);
        return "/manager/lookHotel";
    }

    /**
     * 修改酒店信息
     * @param hotelName
     * @param hotelPhone
     * @param hotelLocation
     * @param singleRoomPrice
     * @param doubleRoomPrice
     * @param totalSingleRoom
     * @param remainSingleRoom
     * @param totalDoubleRoom
     * @param remainDoubleRoom
     * @param hotelDescription
     * @param model
     * @param session
     * @return
     */
    @RequestMapping("/manager/updateHotel")
    public String updateHotel(@RequestParam("hotelName") String hotelName,
                              @RequestParam("hotelPhone") String hotelPhone,
                              @RequestParam("hotelLocation") String hotelLocation,
                              @RequestParam("singleRoomPrice") int singleRoomPrice,
                              @RequestParam("doubleRoomPrice") int doubleRoomPrice,
                              @RequestParam("totalSingleRoom") int totalSingleRoom,
                              @RequestParam("remainSingleRoom") int remainSingleRoom,
                              @RequestParam("totalDoubleRoom") int totalDoubleRoom,
                              @RequestParam("remainDoubleRoom") int remainDoubleRoom,
                              @RequestParam("hotelDescription") String hotelDescription,
                              @RequestParam("hotelId") int hotelId,
                              Model model, HttpSession session){
        hotelMapper.updateHotel(hotelName,hotelId,hotelPhone,hotelLocation,singleRoomPrice,doubleRoomPrice,
                totalSingleRoom,remainSingleRoom,totalDoubleRoom,remainDoubleRoom,hotelDescription);
        HotelVO hotel = hotelMapper.getHotelByHotelId(hotelId);
        model.addAttribute("hotel",hotel);
        return "/manager/lookHotel";
    }
}
