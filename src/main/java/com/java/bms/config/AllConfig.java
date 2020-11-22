package com.java.bms.config;

import com.java.bms.component.LoginHandleInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AllConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
//        浏览器发送/zenglin请求来到success页面
        registry.addViewController("/zenglin").setViewName("success");
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer(){
        WebMvcConfigurer adapter = new WebMvcConfigurer() {

            //注册拦截器
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new LoginHandleInterceptor()).addPathPatterns("/**")
                        .excludePathPatterns("/index.html","/","/common/login","/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg",
                                "/**/*.jpeg", "/**/*.gif", "/**/fonts/*", "/**/*.svg");
            }

            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/").setViewName("index");
                registry.addViewController("/index.html").setViewName("index");
                registry.addViewController("/commonMain").setViewName("common/main.html");



            }
        };
        return adapter;
    }
}
