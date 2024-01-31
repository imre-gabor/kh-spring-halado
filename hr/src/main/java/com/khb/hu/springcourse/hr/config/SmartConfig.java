package com.khb.hu.springcourse.hr.config;


import com.khb.hu.springcourse.hr.service.DefaultEmployeeService;
import com.khb.hu.springcourse.hr.service.EmployeeService;
import com.khb.hu.springcourse.hr.service.SmartEmployeeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("smart")
public class SmartConfig {

    @Bean
    public EmployeeService employeeService(){
        return new SmartEmployeeService();
    }
}
