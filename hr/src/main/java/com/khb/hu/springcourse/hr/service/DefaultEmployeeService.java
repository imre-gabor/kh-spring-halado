package com.khb.hu.springcourse.hr.service;

import com.khb.hu.springcourse.hr.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DefaultEmployeeService extends AbstractEmployeeService {

//    @Value("${hr.payRaise.def}")
//    private int defaultPayRaise;

    @Autowired
    private HrConfigurationProperties hrConfig;

    @Override
    public int getPayRaisePercent(Employee employee) {
        return hrConfig.getPayRaise().getDef();
    }
}
