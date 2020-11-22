package com.java.bms.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@Configuration
public class DruidConfig {
    @ConfigurationProperties("spring.datasource")
    @Bean
    public DataSource druid(){
        return new DruidDataSource();
    }


    //配置Druid监控
    //1.配置一个管理后台的Servlet
    @Bean
    public ServletRegistrationBean statViewServlet(){
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
        Map<String,String> map = new HashMap<>();

        map.put("loginUsername","root");
        map.put("loginPassword","123456");
        map.put("allow","");//默认就是允许所有访问
        map.put("deny","192.168.15.21");//拒绝谁访问

        bean.setInitParameters(map);
        return bean;
    }


    //2.配置一个web监控的filter

    public FilterRegistrationBean webStatFilter(){
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new WebStatFilter());

        Map<String,String> map = new HashMap<>();

        map.put("exclusion","*.js,*.css,/druid/*");

        bean.setInitParameters(map);
        bean.setUrlPatterns(Arrays.asList("/*"));
        return bean;
    }
}
