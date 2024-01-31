package com.khb.hu.springcourse.hr.web;

import com.khb.hu.springcourse.hr.mapper.EmployeeMapper;
import com.khb.hu.springcourse.hr.repository.EmployeeRepository;
import com.khb.hu.springcourse.hr.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/employees")
public class EmployeeControllerOld {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeMapper employeeMapper;

//    @PostMapping
//    public EmployeeDto create(@RequestBody EmployeeDto employee) {
//        employee.setId(null);
//        return employeeMapper.employeeToDto(
//                employeeRepository.save(
//                        employeeMapper.dtoToEmployee(employee)));
//    }

//    @GetMapping("/{id}")
//    public EmployeeDto findById(@PathVariable int id) {
//        Employee employee = employeeRepository.findByIdWithCompanyAndAddresses(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//        return employeeMapper.employeeToDto(employee);
//    }

//    @GetMapping("/{id}")
//    public ResponseEntity<Employee> findById(@PathVariable int id) {
//        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
//        if(optionalEmployee.isPresent()){
//            return ResponseEntity.ok(optionalEmployee.get());
//        } else {
////            return ResponseEntity
////                    .status(HttpStatus.NOT_FOUND)
//                    //.body(sdsdf)
//                    //.headers(headers -> headers.add("", ""))
////                    .build();
//
//            return ResponseEntity.notFound().build();
//        }
//    }

//    @GetMapping
//    public List<EmployeeDto> findAll() {
//        return employeeMapper.employeesToDtos(employeeRepository.findAll());
//    }
//
//    @DeleteMapping("/{id}")
//    public void delete(@PathVariable int id){
//        employeeRepository.deleteById(id);
//    }
//
//    @PutMapping("/{id}")
//    public EmployeeDto modify(@PathVariable int id, @RequestBody EmployeeDto employee){
//        employee.setId(id);
//        Employee modifiedEmployee = employeeService.modify(employeeMapper.dtoToEmployee(employee));
//        if(modifiedEmployee != null) {
//            return employeeMapper.employeeToDto(modifiedEmployee);
//        } else{
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//        }
//    }
//
//    @PostMapping(value = "/{id}/images", consumes = "multipart/form-data")
//    public String uploadImageForEmployee(@PathVariable int id, @RequestPart MultipartFile content) throws IOException {
//        String fileName = employeeService.saveImage(id, content.getInputStream());
//        return "/api/images/" + id + "/" + fileName;
//    }

    @GetMapping("/generateReport")
    @Async
    public CompletableFuture<String> generateReport(){
        CompletableFuture<String> result = employeeService.longRunning();
        //version 1: no result in response, void method, users get the result some other way

        //version 2: we wait for the result, blocking the IO thread --> not recommended
        /*
        try {
            return result.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }*/

        //version 3: controller method is async, it is not the IO thread that is blocked
        return result;
    }
}
