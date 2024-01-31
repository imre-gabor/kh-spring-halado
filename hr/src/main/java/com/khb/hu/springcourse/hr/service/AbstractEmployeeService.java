package com.khb.hu.springcourse.hr.service;

import com.google.common.io.ByteStreams;
import com.khb.hu.springcourse.hr.api.model.EmployeeDto;
import com.khb.hu.springcourse.hr.model.Employee;
import com.khb.hu.springcourse.hr.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.scheduling.annotation.Async;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractEmployeeService implements EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Employee modify(Employee employee) {
        if(employeeRepository.existsById(employee.getId())) {
            return employeeRepository.save(employee);
        } else {
            return null;
        }
    }

    @Override
    public String saveImage(int id, InputStream is) throws IOException {
        employeeRepository.findById(id).get();

        Path folder = Path.of("/temp/kh-hr/employee/" + id);
        Files.createDirectories(folder);

        UUID uuid = UUID.randomUUID();
        try(FileOutputStream os = new FileOutputStream(folder + "/" + uuid)){
            ByteStreams.copy(is, os);
        }
        return uuid.toString();
    }

    @Override
    @Async
    public CompletableFuture<String> longRunning() {
        try {
            Thread.sleep(5000);
            System.out.println("longRunning is ready");
            return CompletableFuture.completedFuture("abcdef");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
