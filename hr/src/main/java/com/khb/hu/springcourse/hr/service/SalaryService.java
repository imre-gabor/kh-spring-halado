package com.khb.hu.springcourse.hr.service;


import com.khb.hu.springcourse.hr.model.Employee;
import com.khb.hu.springcourse.hr.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SalaryService {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    public void setNewSalary(Employee employee){
        int payRaisePercent = employeeService.getPayRaisePercent(employee);
        employee.setSalary(employee.getSalary() * (100 + payRaisePercent) / 100);
    }

//    @Scheduled(cron = "${salaryreport.cron}")
    public void generateSalaryReport(){
        employeeRepository.findAverageSalaries()
                .forEach(stat ->{
                    System.out.format("Average salary at company %d at job %s is %f%n", stat.getCompanyId(), stat.getJob(), stat.getAverageSalary());
                });
    }

}
