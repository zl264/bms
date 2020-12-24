package com.java.bms.other.controller;

import com.java.bms.other.DO.VerifyCodeDO;
import com.java.bms.other.utils.VerifyCodeGen;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码的controller
 */
@Controller
public class OtherController {

    /**
     * 获取验证码
     * @param request
     * @param response
     */
    @RequestMapping("/verifyCode")
    public void verifyCode(HttpServletRequest request, HttpServletResponse response) {
        VerifyCodeGen iVerifyCodeGen = new VerifyCodeGen();
        try {
            //设置长宽
            VerifyCodeDO verifyCode = iVerifyCodeGen.generate(80, 28);
            String code = verifyCode.getCode();

            //将VerifyCode绑定session
            request.getSession().setAttribute("VerifyCode", code);
            //设置响应头
            response.setHeader("Pragma", "no-cache");
            //设置响应头
            response.setHeader("Cache-Control", "no-cache");
            //在代理服务器端防止缓冲
            response.setDateHeader("Expires", 0);
            //设置响应内容类型
            response.setContentType("image/jpeg");
            response.getOutputStream().write(verifyCode.getImgBytes());
            response.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
