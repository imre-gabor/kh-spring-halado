package com.khb.hu.springcourse.hr.service;

import com.khb.hu.springcourse.hr.model.Employee;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

public interface EmployeeService {

    int getPayRaisePercent(Employee employee);

    Employee modify(Employee employee);

    String saveImage(int id, InputStream inputStream) throws IOException;

    CompletableFuture<String> longRunning();

}
