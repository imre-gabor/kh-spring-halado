package com.khb.hu.springcourse.hr.web;

import com.khb.hu.springcourse.hr.api.model.EmployeeDto;
import com.khb.hu.springcourse.hr.model.Employee;
import com.khb.hu.springcourse.hr.repository.EmployeeRepository;
import com.khb.hu.springcourse.hr.util.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class EmployeeControllerWebClientIT extends IntegrationTestBase {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    WebTestClient webTestClient;

    @Test
    void findAll() throws Exception {
        Employee employee =
            employeeRepository.save(
                new Employee(null, "name", "job", 300000, LocalDate.of(2021, 10, 1))
            );

        List<EmployeeDto> body = webTestClient.get()
                .uri("/api/employees")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EmployeeDto.class)
                .returnResult()
                .getResponseBody();
        assertThat(body).hasSize(1);
        assertThat(body.get(0))
                .usingRecursiveComparison()
                .isEqualTo(employee);
    }
}