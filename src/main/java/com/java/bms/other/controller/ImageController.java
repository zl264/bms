package com.java.bms.other.controller;

import com.java.bms.common.VO.CommonUserVO;
import com.java.bms.common.VO.CongressVO;
import com.java.bms.common.mapper.CommonMapper;
import com.java.bms.driver.VO.DriverVO;
import com.java.bms.driver.mapper.DriverMapper;
import com.java.bms.hotel.VO.HotelVO;
import com.java.bms.hotel.mapper.HotelMapper;
import com.java.bms.other.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;

@Controller
public class ImageController {
    @Value("${web.upload-path}")
    private String path;

    @Autowired
    CommonMapper commonMapper;

    @Autowired
    HotelMapper hotelMapper;

    @Autowired
    DriverMapper driverMapper;

    /**
     * 更换普通用户头像
     * @param file 上传的图片
     * @return
     */
    @ResponseBody
    @RequestMapping("/common/image")
    public void updateCommonHeadImage(@RequestParam("file") MultipartFile file,
                                      @RequestParam("commonId") int commonId,Model model,
                         HttpSession session){
        //1定义要上传文件 的存放路径
        String localPath = path +"head/";
        //2获得文件名字
        String fileName=file.getOriginalFilename();
        //2上传失败提示
        String warning="";
        String newFileName = FileUtils.upload(file, localPath, fileName);
        if(newFileName != null){
            //上传成功
            warning="上传成功";
//            int commonId = commonMapper.getCommonIdByUsername((String) session.getAttribute("loginUser"));
            CommonUserVO user = commonMapper.HaveInfomation(commonMapper.getUsernameById(commonId));
            //删除原来的头像
            if(user.getImage()!=null){
                String originalPath = path + "/head/" + user.getImage();
                File originalFile = new File(originalPath);
                if (originalFile.exists()){//文件是否存在
                    originalFile.delete();//删除文件
                }
            }
            commonMapper.updateCommonHeadImage(newFileName,commonId);
            user = commonMapper.HaveInfomation(commonMapper.getUsernameById(commonId));
            model.addAttribute("changeImage",warning);
            model.addAttribute("user", user);
        }else{
            warning="上传失败";
            model.addAttribute("changeImage",warning);
        }
        System.out.println(newFileName);
    }

    /**
     * 更换会议图像
     * @param file 上传的图片
     * @return
     */
    @ResponseBody
    @RequestMapping("/congress/image")
    public void updateCongressImage(@RequestParam("file") MultipartFile file,
                                    @RequestParam("congressId") int congressId,
                                    Model model, HttpSession session){
        //1定义要上传文件 的存放路径
        String localPath = path +"congress/";
        //2获得文件名字
        String fileName=file.getOriginalFilename();
        //2上传失败提示
        String warning="";
        String newFileName = FileUtils.upload(file, localPath, fileName);
        if(newFileName != null){
            //上传成功
            warning="上传成功";
            CongressVO congress = commonMapper.getCongressById(congressId);
            //删除原来的头像
            if(congress.getImage()!=null){
                String originalPath = path + "/congress/" + congress.getImage();
                File originalFile = new File(originalPath);
                if (originalFile.exists()){//文件是否存在
                    originalFile.delete();//删除文件
                }
            }
            commonMapper.updateCongressImage(newFileName,congressId);

            congress = commonMapper.getCongressById(congressId);
            model.addAttribute("congress",congress);

        }else{
            warning="上传失败";
            model.addAttribute("changeImage",warning);
        }
        System.out.println(newFileName);
//        return warning;
    }


    /**
     * 更换会议图像
     * @param file 上传的图片
     * @return
     */
    @ResponseBody
    @RequestMapping("/hotel/image")
    public void updateHotelImage(@RequestParam("file") MultipartFile file,
                                 @RequestParam("hotelId") int hotelId,
                                    Model model, HttpSession session){
        //1定义要上传文件 的存放路径
        String localPath = path +"hotel/";
        //2获得文件名字
        String fileName=file.getOriginalFilename();
        //2上传失败提示
        String warning="";
        String newFileName = FileUtils.upload(file, localPath, fileName);
        if(newFileName != null){
            //上传成功
            warning="上传成功";
//            int hotelId = hotelMapper.getHotelIdByHotelUsername((String)session.getAttribute("hotelUsername"));
            HotelVO hotel = hotelMapper.getHotelByHotelId(hotelId);
            //删除原来的头像
            if(hotel.getImage()!=null){
                String originalPath = path + "/hotel/" + hotel.getImage();
                File originalFile = new File(originalPath);
                if (originalFile.exists()){//文件是否存在
                    originalFile.delete();//删除文件
                }
            }
            hotelMapper.updateHotelImage(newFileName,hotelId);

            hotel = hotelMapper.getHotelByHotelId(hotelId);
            model.addAttribute("hotel",hotel);
            session.setAttribute("hotel",hotel);
        }else{
            warning="上传失败";
            model.addAttribute("changeImage",warning);
        }
        System.out.println(newFileName);
//        return warning;
    }


    /**
     * 更换会议图像
     * @param file 上传的图片
     * @return
     */
    @ResponseBody
    @RequestMapping("/driver/image")
    public void updateDriverImage(@RequestParam("file") MultipartFile file,
                                  @RequestParam("driverId") int driverId,
                                 Model model, HttpSession session){
        //1定义要上传文件 的存放路径
        String localPath = path +"driver/";
        //2获得文件名字
        String fileName=file.getOriginalFilename();
        //2上传失败提示
        String warning="";
        String newFileName = FileUtils.upload(file, localPath, fileName);
        if(newFileName != null){
            //上传成功
            warning="上传成功";
//            int hotelId = hotelMapper.getHotelIdByHotelUsername((String)session.getAttribute("hotelUsername"));
            DriverVO driver = driverMapper.getDriverByDriverId(driverId);
            //删除原来的头像
            if(driver.getImage()!=null){
                String originalPath = path + "/driver/" + driver.getImage();
                File originalFile = new File(originalPath);
                if (originalFile.exists()){//文件是否存在
                    originalFile.delete();//删除文件
                }
            }
            driverMapper.updateDriverImage(newFileName,driverId);

            driver = driverMapper.getDriverByDriverId(driverId);
            model.addAttribute("driver",driver);

        }else{
            warning="上传失败";
            model.addAttribute("changeImage",warning);
        }
        System.out.println(newFileName);
//        return warning;
    }
}
