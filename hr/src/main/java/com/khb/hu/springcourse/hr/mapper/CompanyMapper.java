package com.khb.hu.springcourse.hr.mapper;

import com.khb.hu.springcourse.hr.api.model.CompanyDto;
import com.khb.hu.springcourse.hr.api.model.EmployeeDto;
import com.khb.hu.springcourse.hr.model.Company;
import com.khb.hu.springcourse.hr.model.Employee;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
    @Named("full")
    CompanyDto companyToDto(Company company);

    @IterableMapping(qualifiedByName = "full")
    List<CompanyDto> companiesToDtos(List<Company> company);
    Company dtoToCompany(CompanyDto companyDto);

    @Mapping(target = "company", ignore = true)
    EmployeeDto employeeDto(Employee employee);

    @Mapping(target = "employees", ignore = true)
    @Mapping(target = "addresses", ignore = true)
    CompanyDto companyToDtoSummary(Company company);
}
