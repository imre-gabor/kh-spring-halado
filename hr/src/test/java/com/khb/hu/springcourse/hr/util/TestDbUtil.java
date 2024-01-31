package com.khb.hu.springcourse.hr.util;

import com.khb.hu.springcourse.hr.repository.AddressRepository;
import com.khb.hu.springcourse.hr.repository.CompanyRepository;
import com.khb.hu.springcourse.hr.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestDbUtil {


    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    CompanyRepository companyRepository;

    public void clearDb(){
        addressRepository.deleteAllInBatch();
        employeeRepository.deleteAllInBatch();
        companyRepository.deleteAllInBatch();
    }

}
