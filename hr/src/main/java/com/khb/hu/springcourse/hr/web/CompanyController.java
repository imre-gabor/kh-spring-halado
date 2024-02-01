package com.khb.hu.springcourse.hr.web;

import com.google.common.collect.Lists;
import com.khb.hu.springcourse.hr.api.CompanyControllerApi;
import com.khb.hu.springcourse.hr.api.model.CompanyDto;
import com.khb.hu.springcourse.hr.api.model.EmployeeDto;
import com.khb.hu.springcourse.hr.mapper.CompanyMapper;
import com.khb.hu.springcourse.hr.mapper.EmployeeMapper;
import com.khb.hu.springcourse.hr.model.Company;
import com.khb.hu.springcourse.hr.repository.CompanyRepository;
import com.khb.hu.springcourse.hr.service.CompanyService;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
public class CompanyController implements CompanyControllerApi {

    @Autowired
    private NativeWebRequest webRequest;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.of(webRequest);
    }

    @Override
    public ResponseEntity<CompanyDto> addEmployee(Integer id, EmployeeDto employeeDto) {
        return ResponseEntity.ok(companyMapper.companyToDto(companyService.addEmployee(id, employeeMapper.dtoToEmployee(employeeDto))));
    }

    @Override
    public ResponseEntity<CompanyDto> createCompany(CompanyDto companyDto) {
        companyDto.setId(null);
        return ResponseEntity.ok(companyMapper.companyToDto(
                companyRepository.save(
                        companyMapper.dtoToCompany(companyDto))));
    }

    @Override
    public ResponseEntity<Void> deleteCompany(Integer id) {
        companyRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<CompanyDto>> findAllCompanies() {
        return ResponseEntity.ok(companyMapper.companiesToDtos(companyRepository.findAll()));
    }

    @Override
    public ResponseEntity<CompanyDto> findCompanyById(Integer id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(companyMapper.companyToDto(company));
    }

    @PostMapping("/api/companies/search")
    public ResponseEntity<List<CompanyDto>> search(@RequestBody CompanyDto example, @SortDefault("id") Pageable pageable, @RequestParam Optional<Boolean> full){
        List<CompanyDto> result = null;
        final Page<Company> foundPage;
        if(full.orElse(false)) {
            foundPage = companyService.findByExampleWithSpecificationPagedAndFetchAllRelationships(companyMapper.dtoToCompany(example), pageable);
            result = foundPage.map(companyMapper::companyToDto).getContent();

        } else {
            foundPage = companyService.findByExampleWithSpecificationPaged(companyMapper.dtoToCompany(example), pageable);
            result = foundPage.map(companyMapper::companyToDtoSummary).getContent();
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers -> headers.add("x-total-count",String.valueOf(foundPage.getTotalElements())))
                .body(result);

    }

    @PostMapping("/api/companies/search2")
    public ResponseEntity<List<CompanyDto>> search2(@RequestBody CompanyDto example){
        List<CompanyDto> result =
            companyMapper.companiesToDtoSummaries(
                companyService.findByExampleWithQuerydsl(companyMapper.dtoToCompany(example)));

        return ResponseEntity
                .ok(result);
    }

    @GetMapping("/api/companies/search3")
    public ResponseEntity<List<CompanyDto>> search3(
            @QuerydslPredicate(root = Company.class) Predicate predicate,
            @SortDefault("id") Pageable pageable
    ){
        List<CompanyDto> result =
                companyMapper.companiesToDtos(Lists.newArrayList(
                        companyService.findByExampleWithQuerydslPagedAndFetchAllRelationships(predicate, pageable)));

        return ResponseEntity
                .ok(result);
    }

}
