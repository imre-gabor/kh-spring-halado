package com.khb.hu.springcourse.hr.web;

import com.khb.hu.springcourse.hr.mapper.EmployeeMapper;
import com.khb.hu.springcourse.hr.model.Employee;
import com.khb.hu.springcourse.hr.repository.EmployeeRepository;
import com.khb.hu.springcourse.hr.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;


@ExtendWith({SpringExtension.class})
//@ActiveProfiles("nosec") --> works only with @SpringBootTest, not with @WebMvcTest.
//With @WebMvcTest, SecurityAutoConfiguration should be excluded as follows:
@WebMvcTest(controllers = EmployeeController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class EmployeeControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    EmployeeRepository employeeRepository;

    @MockBean
    EmployeeService employeeService;

    @MockBean
    EmployeeMapper employeeMapper;


    @Test
    void findAll() throws Exception {
        Employee employee = new Employee(1, "name", "job", 300000, LocalDate.of(2021, 10, 1));
        List<Employee> employeeList = Arrays.asList(employee);
        given(employeeRepository.findAll())
                .willReturn(employeeList);
        given(employeeMapper.employeesToDtos(employeeList))
                .willReturn(EmployeeMapper.INSTANCE.employeesToDtos(employeeList));

        mvc.perform(
                get("/api/employees")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(employee.getId())))
                .andExpect(jsonPath("$[0].name", is(employee.getName())))
                .andExpect(jsonPath("$[0].job", is(employee.getJob())))
                .andExpect(jsonPath("$[0].salary", is(employee.getSalary())))
                .andExpect(jsonPath("$[0].workStart", is(employee.getWorkStart().toString())));
    }
}