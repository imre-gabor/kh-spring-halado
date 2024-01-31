package com.khb.hu.springcourse.hr.mapper;

import com.khb.hu.springcourse.hr.api.model.CompanyDto;
import com.khb.hu.springcourse.hr.api.model.EmployeeDto;
import com.khb.hu.springcourse.hr.model.Company;
import com.khb.hu.springcourse.hr.model.Employee;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);


    EmployeeDto employeeToDto(Employee employee);

    @IterableMapping(qualifiedByName = "summary")
    List<EmployeeDto> employeesToDtos(List<Employee> employee);

    @Named("summary")
    @Mapping(target = "company", ignore = true)
    EmployeeDto employeeToDtoSummary(Employee employee);

    Employee dtoToEmployee(EmployeeDto employeeDto);

    @Mapping(target = "employees", ignore = true)
    CompanyDto companyToDto(Company company);

}
