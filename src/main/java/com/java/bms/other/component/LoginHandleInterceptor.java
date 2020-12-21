package com.java.bms.other.component;

import com.java.bms.other.config.AllConfig;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录检查
 * 检查用户有没有登录，如果没有登录则不允许访问其他的界面，并跳转会登录界面
 */
public class LoginHandleInterceptor implements HandlerInterceptor {

    //目标方法执行之前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object user = request.getSession().getAttribute("loginUser");
        Object hotelUser = request.getSession().getAttribute("hotelUsername");
        if(user==null&&hotelUser==null){
            //未登录
            request.setAttribute("msg","没有权限请先登录");
            request.getRequestDispatcher("/").forward(request,response);
//            response.sendRedirect("/");
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
