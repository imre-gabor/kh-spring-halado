package com.khb.hu.springcourse.hr.service;

import com.khb.hu.springcourse.hr.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class SmartEmployeeService  extends AbstractEmployeeService {
    @Autowired
    private HrConfigurationProperties hrConfig;

    @Override
    public int getPayRaisePercent(Employee employee) {
        long yearsBetween = ChronoUnit.YEARS.between(employee.getWorkStart(), LocalDate.now());
        HrConfigurationProperties.PayRaise payRaise = hrConfig.getPayRaise();

        return yearsBetween > payRaise.getLimit()
                ? payRaise.getPremium()
                : payRaise.getDef();
    }
}
