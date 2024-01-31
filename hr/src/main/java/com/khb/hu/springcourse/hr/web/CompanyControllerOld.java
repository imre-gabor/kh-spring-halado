//package com.khb.hu.springcourse.hr.web;
//
//import com.khb.hu.springcourse.hr.dto.CompanyDto;
//import com.khb.hu.springcourse.hr.dto.EmployeeDto;
//import com.khb.hu.springcourse.hr.mapper.CompanyMapper;
//import com.khb.hu.springcourse.hr.mapper.EmployeeMapper;
//import com.khb.hu.springcourse.hr.model.Company;
//import com.khb.hu.springcourse.hr.repository.CompanyRepository;
//import com.khb.hu.springcourse.hr.service.CompanyService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.web.SortDefault;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.List;
//import java.util.Optional;
//
////@RestController
////@RequestMapping("/api/companies")
//public class CompanyControllerOld {
//
//    @Autowired
//    private CompanyRepository companyRepository;
//
//    @Autowired
//    private CompanyService companyService;
//
//    @Autowired
//    private CompanyMapper companyMapper;
//
//    @Autowired
//    private EmployeeMapper employeeMapper;
//
//    @PostMapping
//    public CompanyDto create(@RequestBody CompanyDto company) {
//        company.setId(null);
//        return companyMapper.companyToDto(
//                companyRepository.save(
//                        companyMapper.dtoToCompany(company)));
//    }
//
//    @GetMapping("/{id}")
//    public CompanyDto findById(@PathVariable int id) {
//        Company company = companyRepository.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//        return companyMapper.companyToDto(company);
//    }
//
//    @GetMapping
//    public List<CompanyDto> findAll() {
//        return companyMapper.companiesToDtos(companyRepository.findAll());
//    }
//
//    @DeleteMapping("/{id}")
//    public void delete(@PathVariable int id){
//
//        companyRepository.deleteById(id);
//    }
//
//    @PostMapping("/{id}/employees")
//    public CompanyDto addEmployee(@PathVariable int id, @RequestBody EmployeeDto employeeDto){
//        return companyMapper.companyToDto(companyService.addEmployee(id, employeeMapper.dtoToEmployee(employeeDto)));
//    }
//
//    @PostMapping("/search")
//    public ResponseEntity<List<CompanyDto>> search(@RequestBody CompanyDto example, @SortDefault("id") Pageable pageable, @RequestParam Optional<Boolean> full){
//        List<CompanyDto> result = null;
//        final Page<Company> foundPage;
//        if(full.orElse(false)) {
//            foundPage = companyService.findByExampleWithSpecificationPagedAndFetchAllRelationships(companyMapper.dtoToCompany(example), pageable);
//            result = foundPage.map(companyMapper::companyToDto).getContent();
//
//        } else {
//            foundPage = companyService.findByExampleWithSpecificationPaged(companyMapper.dtoToCompany(example), pageable);
//            result = foundPage.map(companyMapper::companyToDtoSummary).getContent();
//        }
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .headers(headers -> headers.add("x-total-count",String.valueOf(foundPage.getTotalElements())))
//                .body(result);
//
//    }
//
//}
