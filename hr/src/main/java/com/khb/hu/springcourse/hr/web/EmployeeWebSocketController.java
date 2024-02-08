package com.khb.hu.springcourse.hr.web;

import com.khb.hu.springcourse.hr.api.model.EmployeeDto;
import com.khb.hu.springcourse.hr.mapper.EmployeeMapper;
import com.khb.hu.springcourse.hr.model.Employee;
import com.khb.hu.springcourse.hr.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class EmployeeWebSocketController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/employees/modify")
    public void modify(EmployeeDto employeeDto) {

        Employee modifiedEmployee = employeeService.modify(employeeMapper.dtoToEmployee(employeeDto));
        if(modifiedEmployee != null) {
            EmployeeDto modifiedDto = employeeMapper.employeeToDto(modifiedEmployee);
            simpMessagingTemplate.convertAndSend("/topic/employees/" + modifiedDto.getId(), modifiedDto);
        } else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
