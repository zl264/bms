package com.java.bms.other.config;

import com.java.bms.other.component.LoginHandleInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 这是整个项目的全局配置类
 */
@Configuration
public class AllConfig implements WebMvcConfigurer {

    //不拦截的url
    private static List<String> excludeUrl = new ArrayList<String>(Arrays.asList("/index.html","/","/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg",
            "/**/*.jpeg", "/**/*.gif", "/**/fonts/*", "/**/*.svg","/common/login","/common/register",
            "/driver/login","/driver/register","/hotel/login","/hotel/register","/manager/login",
            "/common/enter","/common/enterRegister","/hotel/enter","/hotel/enterRegister","/manager/enter","/driver/enter","/driver/enterRegister",
            "/common/commonLogin","/common/commonRegister","/hotel/hotelLogin","/hotel/hotelRegister","/manager/managerLogin","/driver/driverLogin","/driver/driverRegister",
            "/verifyCode"
    ));

    @Bean
    public WebMvcConfigurer webMvcConfigurer(){
        WebMvcConfigurer adapter = new WebMvcConfigurer() {

            //注册拦截器
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new LoginHandleInterceptor()).addPathPatterns("/**")
                        .excludePathPatterns(excludeUrl);
            }

            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/").setViewName("index");
                registry.addViewController("/index.html").setViewName("index");
                registry.addViewController("/commonMain").setViewName("/common/main");
                registry.addViewController("/driverMain").setViewName("/driver/main");
                registry.addViewController("/hotelMain").setViewName("/hotel/main");
                registry.addViewController("/managerMain").setViewName("/manager/main");
                registry.addViewController("/index").setViewName("/index");

            }
        };
        return adapter;
    }


//    public static void addUrl(String url){
//        excludeUrl.add(url);
//        AllConfig allConfig = new AllConfig();
//    }
//
//    public static void deleteUrl(String url){
//        excludeUrl.remove(url);
//    }
//
//    public static boolean isContainUrl(String url){
//        return excludeUrl.contains(url);
//    }
//
//    public static void show(){
//        excludeUrl.forEach(System.out::println);
//    }
}
