package com.khb.hu.springcourse.hr.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khb.hu.springcourse.hr.model.Employee;
import com.khb.hu.springcourse.hr.repository.EmployeeRepository;
import com.khb.hu.springcourse.hr.util.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@ActiveProfiles("nosec")
class EmployeeControllerIT extends IntegrationTestBase {

    @Autowired
    MockMvc mvc;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void findAll() throws Exception {
        Employee employee =
            employeeRepository.save(
                new Employee(null, "name", "job", 300000, LocalDate.of(2021, 10, 1))
            );

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

    @Test
    void save() throws Exception {

        Employee employee = new Employee(null, "name", "job", 300000, LocalDate.of(2021, 10, 1));
        String responseBodyString = mvc.perform(post("/api/employees")
                        .content(objectMapper.writeValueAsString(employee))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        int newId = objectMapper.readValue(responseBodyString, Employee.class).getId();

        mvc.perform(
                        get("/api/employees")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(newId)))
                .andExpect(jsonPath("$[0].name", is(employee.getName())))
                .andExpect(jsonPath("$[0].job", is(employee.getJob())))
                .andExpect(jsonPath("$[0].salary", is(employee.getSalary())))
                .andExpect(jsonPath("$[0].workStart", is(employee.getWorkStart().toString())));
    }
}