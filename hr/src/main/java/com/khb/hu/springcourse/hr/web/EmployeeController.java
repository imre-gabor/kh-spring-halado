package com.khb.hu.springcourse.hr.web;

import com.khb.hu.springcourse.hr.api.EmployeeControllerApi;
import com.khb.hu.springcourse.hr.api.model.EmployeeDto;
import com.khb.hu.springcourse.hr.mapper.EmployeeMapper;
import com.khb.hu.springcourse.hr.model.Employee;
import com.khb.hu.springcourse.hr.repository.EmployeeRepository;
import com.khb.hu.springcourse.hr.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class EmployeeController implements EmployeeControllerApi {

    @Autowired
    NativeWebRequest webRequest;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.of(webRequest);
    }

    @Override
    public ResponseEntity<EmployeeDto> create(EmployeeDto employee) {
        employee.setId(null);
        return ResponseEntity.ok(employeeMapper.employeeToDto(
                employeeRepository.save(
                        employeeMapper.dtoToEmployee(employee))));
    }

    @Override
    public ResponseEntity<Void> delete(Integer id) {
        employeeRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<EmployeeDto>> findAll() {

        return ResponseEntity.ok(employeeMapper.employeesToDtos(employeeRepository.findAll()));
    }

    @Override
    public ResponseEntity<EmployeeDto> findById(Integer id) {
        Employee employee = employeeRepository.findByIdWithCompanyAndAddresses(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(employeeMapper.employeeToDto(employee));

    }

    @Override
    public ResponseEntity<EmployeeDto> modify(Integer id, EmployeeDto employeeDto) {
        employeeDto.setId(id);
        Employee modifiedEmployee = employeeService.modify(employeeMapper.dtoToEmployee(employeeDto));
        if(modifiedEmployee != null) {
            return ResponseEntity.ok(employeeMapper.employeeToDto(modifiedEmployee));
        } else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<String> uploadImageForEmployee(Integer id, MultipartFile content) {
        try {
            String fileName = employeeService.saveImage(id, content.getInputStream());
            return ResponseEntity.ok("/api/images/" + id + "/" + fileName);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
