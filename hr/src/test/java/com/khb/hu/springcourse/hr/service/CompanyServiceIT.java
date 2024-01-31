package com.khb.hu.springcourse.hr.service;

import com.khb.hu.springcourse.hr.model.Address;
import com.khb.hu.springcourse.hr.model.Company;
import com.khb.hu.springcourse.hr.model.Employee;
import com.khb.hu.springcourse.hr.repository.AddressRepository;
import com.khb.hu.springcourse.hr.repository.CompanyRepository;
import com.khb.hu.springcourse.hr.repository.EmployeeRepository;
import static org.assertj.core.api.Assertions.assertThat;

import com.khb.hu.springcourse.hr.util.IntegrationTestBase;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
class CompanyServiceIT extends IntegrationTestBase {

    @Autowired
    CompanyService companyService;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    AddressRepository addressRepository;


    @Test
    void findByExample_ById() {
        List<Company> companies = saveDefaultTestCompanies();

        List<Company> found = companyService.findByExample(new Company(companies.get(1).getId()));

        assertThat(found).containsExactly(companies.get(1));
    }

    @Test
    void findByExample_ByName() {
        List<Company> companies = saveDefaultTestCompanies();

        List<Company> found = companyService.findByExample(new Company("ny"));

        assertThat(found).containsExactly(companies.get(0), companies.get(1));
    }

    @Test
    void findByExample_ByIdAndName() {
        List<Company> companies = saveDefaultTestCompanies();

        List<Company> found = companyService.findByExample(new Company(companies.get(1).getId(),"ny"));

        assertThat(found).containsExactly(companies.get(1));
    }


    @Test
    void findByExampleWithSpecification_ById() {
        List<Company> companies = saveDefaultTestCompanies();

        List<Company> found = companyService.findByExampleWithSpecification(new Company(companies.get(1).getId()));

        assertThat(found).containsExactly(companies.get(1));
    }

    private List<Company> saveDefaultTestCompanies() {
        List<Company> companies = companyRepository.saveAll(Arrays.asList(
                new Company("company1"),
                new Company("company2"),
                new Company("comp3")));
        return companies;
    }

    @Test
    void findByExampleWithSpecification_ByName() {
        List<Company> companies = saveDefaultTestCompanies();

        List<Company> found = companyService.findByExampleWithSpecification(new Company("ny"));

        assertThat(found).containsExactly(companies.get(0), companies.get(1));
    }

    @Test
    void findByExampleWithSpecification_ByIdAndName() {
        List<Company> companies = saveDefaultTestCompanies();

        List<Company> found = companyService.findByExampleWithSpecification(new Company(companies.get(1).getId(),"ny"));

        assertThat(found).containsExactly(companies.get(1));
    }

    @Test
    void findByExampleWithSpecification_ByNameAndEmployeeName() {
        List<Company> companies = saveDefaultTestCompanies();

        saveNewEmployeeForCompany("John Wayne",
                LocalDate.of(2021, 10, 1),
                companies.get(0));

        saveNewEmployeeForCompany("Bruce Wayne",
                LocalDate.of(2021, 10, 1),
                companies.get(1));

        Company example = new Company("ny");
        example.setEmployees(Arrays.asList(new Employee(null, "John", null, 0.0, null)));
        List<Company> found = companyService.findByExampleWithSpecification(example);

        assertThat(found).containsExactly(companies.get(0));
    }

    @Test
    void findByExampleWithSpecification_ByNameAndEmployeeWorkStart() {
        List<Company> companies = saveDefaultTestCompanies();

        saveNewEmployeeForCompany("John Wayne",
                LocalDate.of(2021, 10, 1),
                companies.get(0));

        saveNewEmployeeForCompany("Bruce Wayne",
                LocalDate.of(2021, 10, 31),
                companies.get(1));

        Company example = new Company("ny");
        example.setEmployees(Arrays.asList(new Employee(null, "", null, 0.0, LocalDate.of(2021, 10, 20))));
        List<Company> found = companyService.findByExampleWithSpecification(example);

        assertThat(found).containsExactly(companies.get(0), companies.get(1));
    }

    @Test
    void findCompanyById_WithLazyFetchedEmployees(){ //cannot pass if EAGER fetch is defined at Company.employees
        List<Company> companies = saveDefaultTestCompanies();

        Company company = companyRepository.findById(companies.get(0).getId()).get();
        Assertions.assertThrows(LazyInitializationException.class, () -> {
            company.getEmployees().iterator();
        });
    }

    @Test
    void findCompanyById_WithEagerFetchedEmployees(){
        List<Company> companies = saveDefaultTestCompanies();
        Employee employee = saveNewEmployeeForCompany("John Wayne",
                LocalDate.of(2021, 10, 1),
                companies.get(0));

        Company company = companyRepository.findByIdWithEmployees(companies.get(0).getId()).get();

        assertThat(company.getEmployees().get(0).getId()).isEqualTo(employee.getId());
    }

    @Test
    void findByExampleWithSpecification_ByNameAndEmployeeName_LazyFetch() {
        List<Company> companies = saveDefaultTestCompanies();

        saveNewEmployeeForCompany("John Wayne",
                LocalDate.of(2021, 10, 1),
                companies.get(0));

        saveNewEmployeeForCompany("Bruce Wayne",
                LocalDate.of(2021, 10, 1),
                companies.get(1));

        Company example = new Company("ny");
        example.setEmployees(Arrays.asList(new Employee(null, "John", null, 0.0, null)));
        List<Company> found = companyService.findByExampleWithSpecification(example);

        assertThat(found).containsExactly(companies.get(0));
        Assertions.assertThrows(LazyInitializationException.class,
                () -> found.get(0).getEmployees().iterator());
    }

    @Test
    void findByExampleWithSpecification_ByNameAndEmployeeName_WithFetchingEmployeesOnly() {
        List<Company> companies = saveDefaultTestCompanies();

        Employee employee = saveNewEmployeeForCompany("John Wayne",
                LocalDate.of(2021, 10, 1),
                companies.get(0));

        Address address = addressRepository.save(new Address("Budapest", "Magyar tudósok körútja 2", "1111"));
        companies.get(0).addAddress(address);
        addressRepository.save(address);

        saveNewEmployeeForCompany("Bruce Wayne",
                LocalDate.of(2021, 10, 1),
                companies.get(1));

        Company example = new Company("ny");
        example.setEmployees(Arrays.asList(new Employee(null, "John", null, 0.0, null)));
        List<Company> found = companyService.findByExampleWithSpecificationAndEntityGraph(example, "Company.withEmployees");

        assertThat(found.get(0).getEmployees().get(0).getId())
                .isEqualTo(employee.getId());
        Assertions.assertThrows(LazyInitializationException.class,
                () -> found.get(0).getAddresses().iterator());
    }

    @Test
    void findByExampleWithSpecification_ByNameAndEmployeeName_WithFetchingEmployeesAndAddresses() {
        List<Company> companies = saveDefaultTestCompanies();

        Employee employee = saveNewEmployeeForCompany("John Wayne",
                LocalDate.of(2021, 10, 1),
                companies.get(0));

        Address address = addressRepository.save(new Address("Budapest", "Magyar tudósok körútja 2", "1111"));
        companies.get(0).addAddress(address);
        addressRepository.save(address);

        saveNewEmployeeForCompany("Bruce Wayne",
                LocalDate.of(2021, 10, 1),
                companies.get(1));

        Company example = new Company("ny");
        example.setEmployees(Arrays.asList(new Employee(null, "John", null, 0.0, null)));
        List<Company> found = companyService.findByExampleWithSpecificationAndEntityGraph(example, "Company.withEmployeesAndAddresses");
        //if addresses and employees were both lists, fetching them at once would not work
        //still a problem with this solution: the result rows at SQL are Cartesian product, e.g.
        //10 companies, with 10 employees and 10 addresses each, would result in 10*10*10 rows
        //we avoid this at the next test case

        assertThat(found.get(0).getEmployees().get(0).getId())
                .isEqualTo(employee.getId());

        assertThat(found.get(0).getAddresses().iterator().next().getId())
                .isEqualTo(address.getId());
    }


    @Test
    void findByExampleWithSpecification_ByNameAndEmployeeName_WithFetchingEmployeesAndAddresses_NoCartesian_Product() {
        List<Company> companies = saveDefaultTestCompanies();

        Employee employee = saveNewEmployeeForCompany("John Wayne",
                LocalDate.of(2021, 10, 1),
                companies.get(0));

        Address address = addressRepository.save(new Address("Budapest", "Magyar tudósok körútja 2", "1111"));
        companies.get(0).addAddress(address);
        addressRepository.save(address);

        saveNewEmployeeForCompany("Bruce Wayne",
                LocalDate.of(2021, 10, 1),
                companies.get(1));

        Company example = new Company("ny");
        example.setEmployees(Arrays.asList(new Employee(null, "John", null, 0.0, null)));
        List<Company> found = companyService.findByExampleWithSpecificationAndFetchAllRelationships(example);

        assertThat(found.get(0).getEmployees().get(0).getId())
                .isEqualTo(employee.getId());

        assertThat(found.get(0).getAddresses().iterator().next().getId())
                .isEqualTo(address.getId());
    }


    @Test
    void findByExampleWithSpecificationPaged_ByNameAndEmployeeName_LazyFetch() {
        List<Company> companies = saveDefaultTestCompanies();

        saveNewEmployeeForCompany("John Wayne",
                LocalDate.of(2021, 10, 1),
                companies.get(0));

        saveNewEmployeeForCompany("Bruce Wayne",
                LocalDate.of(2021, 10, 1),
                companies.get(1));

        Company example = new Company("ny");
        example.setEmployees(Arrays.asList(new Employee(null, "John", null, 0.0, null)));
        Page<Company> foundPage = companyService.findByExampleWithSpecificationPaged(example, PageRequest.of(0, 10, Sort.by("id")));
        assertThat(foundPage.getTotalElements()).isEqualTo(1);

        List<Company> found = foundPage.getContent();
        assertThat(found).containsExactly(companies.get(0));
        Assertions.assertThrows(LazyInitializationException.class,
                () -> found.get(0).getEmployees().iterator());
        Assertions.assertThrows(LazyInitializationException.class,
                () -> found.get(0).getAddresses().iterator());
    }

    @Test
    void findByExampleWithSpecificationPaged_ByNameAndEmployeeName_WithFetchingEmployeesOnly() {
        List<Company> companies = saveDefaultTestCompanies();

        Employee employee = saveNewEmployeeForCompany("John Wayne",
                LocalDate.of(2021, 10, 1),
                companies.get(0));

        Address address = addressRepository.save(new Address("Budapest", "Magyar tudósok körútja 2", "1111"));
        companies.get(0).addAddress(address);
        addressRepository.save(address);

        saveNewEmployeeForCompany("Bruce Wayne",
                LocalDate.of(2021, 10, 1),
                companies.get(1));

        Company example = new Company("ny");
        example.setEmployees(Arrays.asList(new Employee(null, "John", null, 0.0, null)));
        List<Company> found = companyService.findByExampleWithSpecificationPagedAndEntityGraph(
                example, PageRequest.of(0, 10, Sort.by("id")), "Company.withEmployees");
        //this solution causes in-memory paging, fetchingf all rows from DB!!

        assertThat(found.get(0).getEmployees().get(0).getId())
                .isEqualTo(employee.getId());
        Assertions.assertThrows(LazyInitializationException.class,
                () -> found.get(0).getAddresses().iterator());
    }

    @Test
    void findByExampleWithSpecificationPaged_ByNameAndEmployeeName_WithFetchingAllRelationships() {
        List<Company> companies = saveDefaultTestCompanies();

        Employee employee = saveNewEmployeeForCompany("John Wayne",
                LocalDate.of(2021, 10, 1),
                companies.get(0));

        Address address = addressRepository.save(new Address("Budapest", "Magyar tudósok körútja 2", "1111"));
        companies.get(0).addAddress(address);
        addressRepository.save(address);

        saveNewEmployeeForCompany("Bruce Wayne",
                LocalDate.of(2021, 10, 1),
                companies.get(1));

        Company example = new Company("ny");
        example.setEmployees(Arrays.asList(new Employee(null, "John", null, 0.0, null)));
        Page<Company> foundPage = companyService.findByExampleWithSpecificationPagedAndFetchAllRelationships(
                example, PageRequest.of(0, 10, Sort.by("id")));

        assertThat(foundPage.getTotalElements()).isEqualTo(1);
        List<Company> found = foundPage.getContent();

        assertThat(found.get(0).getEmployees().get(0).getId())
                .isEqualTo(employee.getId());
        assertThat(found.get(0).getAddresses().iterator().next().getId())
                .isEqualTo(address.getId());
    }

    private Employee saveNewEmployeeForCompany(String employeeName, LocalDate workStart, Company company) {
        Employee employee = employeeRepository.save(
                new Employee(null, employeeName, "actor", 100000,
                        workStart));
        company.addEmployee(employee);
        return employeeRepository.save(employee);
    }
}