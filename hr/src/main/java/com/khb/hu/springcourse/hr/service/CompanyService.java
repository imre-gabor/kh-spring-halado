package com.khb.hu.springcourse.hr.service;

import com.khb.hu.springcourse.hr.model.Company;
import com.khb.hu.springcourse.hr.model.Employee;
import com.khb.hu.springcourse.hr.repository.CompanyRepository;
import com.khb.hu.springcourse.hr.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

import static com.khb.hu.springcourse.hr.service.CompanySpecifications.*;

import java.time.LocalDate;
import java.util.List;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    IExternalSystemService externalSystemService;

    @PersistenceContext
    private EntityManager em;

    public List<Company> findByExample(Company company) {
        return companyRepository.findAll(
                Example.of(company,
                        ExampleMatcher.matching()
                        .withIgnoreNullValues()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)));
    }

    public List<Company> findByExampleWithSpecification(Company company) {
        Integer id = company.getId();
        String name = company.getName();

        Specification<Company> spec = Specification.where(null);
        if(id != null){
            spec = spec.and(idEquals(id));
        }

        if(StringUtils.hasLength(name)){
            spec = spec.and(nameContains(name));
        }

        if(!CollectionUtils.isEmpty(company.getEmployees())) {
            Employee employee = company.getEmployees().get(0);
            String employeeName = employee.getName();
            LocalDate employeeWorkStart = employee.getWorkStart();
            if(StringUtils.hasLength(employeeName)){
                spec = spec.and(hasEmployeeWithNamePrefix(employeeName));
            }
            if(employeeWorkStart != null){
                spec = spec.and(hasEmployeeStartedWorkingAtMonthOf(employeeWorkStart));
            }
        }

        return companyRepository.findAll(spec);
    }

    public List<Company> findByExampleWithSpecificationAndEntityGraph(Company company, String entityGraphName) {
        Integer id = company.getId();
        String name = company.getName();

        Specification<Company> spec = Specification.where(null);
        if(id != null){
            spec = spec.and(idEquals(id));
        }

        if(StringUtils.hasLength(name)){
            spec = spec.and(nameContains(name));
        }

        if(!CollectionUtils.isEmpty(company.getEmployees())) {
            Employee employee = company.getEmployees().get(0);
            String employeeName = employee.getName();
            LocalDate employeeWorkStart = employee.getWorkStart();
            if(StringUtils.hasLength(employeeName)){
                spec = spec.and(hasEmployeeWithNamePrefix(employeeName));
            }
            if(employeeWorkStart != null){
                spec = spec.and(hasEmployeeStartedWorkingAtMonthOf(employeeWorkStart));
            }
        }

        TypedQuery<Company> query = createQueryWithSpecificationAndEntityGraph(spec, entityGraphName);

        return query.getResultList();

    }

    private TypedQuery<Company> createQueryWithSpecificationAndEntityGraph(Specification<Company> spec, String entityGraphName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Company> cq = cb.createQuery(Company.class);
        Root<Company> root = cq.from(Company.class);

        cq.where(spec.toPredicate(root, cq, cb));

        TypedQuery<Company> query = em.createQuery(cq);

        EntityGraph<?> entityGraph = em.getEntityGraph(entityGraphName);
        query.setHint(org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.LOAD.getKey(), entityGraph);
        return query;
    }


    @Transactional
    public List<Company> findByExampleWithSpecificationAndFetchAllRelationships(Company company) {
        Integer id = company.getId();
        String name = company.getName();

        Specification<Company> spec = Specification.where(null);
        if(id != null){
            spec = spec.and(idEquals(id));
        }

        if(StringUtils.hasLength(name)){
            spec = spec.and(nameContains(name));
        }

        if(!CollectionUtils.isEmpty(company.getEmployees())) {
            Employee employee = company.getEmployees().get(0);
            String employeeName = employee.getName();
            LocalDate employeeWorkStart = employee.getWorkStart();
            if(StringUtils.hasLength(employeeName)){
                spec = spec.and(hasEmployeeWithNamePrefix(employeeName));
            }
            if(employeeWorkStart != null){
                spec = spec.and(hasEmployeeStartedWorkingAtMonthOf(employeeWorkStart));
            }
        }

        List<Company> companies = companyRepository.findAll(spec);
        List<Integer> ids = companies.stream().map(Company::getId).toList();
        companies = companyRepository.findByIdInWithAddresses(ids);
        companies = companyRepository.findByIdInWithEmployees(ids);

        return companies;
    }


    //@PreAuthorize("hasAuthority('SEARCH_COMPANY')")
    @Cacheable("companySearchResults")
    public Page<Company> findByExampleWithSpecificationPaged(Company company, Pageable pageable) {
        externalSystemService.callExternalService2();
        Integer id = company.getId();
        String name = company.getName();

        Specification<Company> spec = Specification.where(null);
        if(id != null){
            spec = spec.and(idEquals(id));
        }

        if(StringUtils.hasLength(name)){
            spec = spec.and(nameContains(name));
        }

        if(!CollectionUtils.isEmpty(company.getEmployees())) {
            Employee employee = company.getEmployees().get(0);
            String employeeName = employee.getName();
            LocalDate employeeWorkStart = employee.getWorkStart();
            if(StringUtils.hasLength(employeeName)){
                spec = spec.and(hasEmployeeWithNamePrefix(employeeName));
            }
            if(employeeWorkStart != null){
                spec = spec.and(hasEmployeeStartedWorkingAtMonthOf(employeeWorkStart));
            }
        }

        return companyRepository.findAll(spec, pageable);
    }

    public List<Company> findByExampleWithSpecificationPagedAndEntityGraph(Company company, Pageable pageable, String entityGraphName) {
        Integer id = company.getId();
        String name = company.getName();

        Specification<Company> spec = Specification.where(null);
        if(id != null){
            spec = spec.and(idEquals(id));
        }

        if(StringUtils.hasLength(name)){
            spec = spec.and(nameContains(name));
        }

        if(!CollectionUtils.isEmpty(company.getEmployees())) {
            Employee employee = company.getEmployees().get(0);
            String employeeName = employee.getName();
            LocalDate employeeWorkStart = employee.getWorkStart();
            if(StringUtils.hasLength(employeeName)){
                spec = spec.and(hasEmployeeWithNamePrefix(employeeName));
            }
            if(employeeWorkStart != null){
                spec = spec.and(hasEmployeeStartedWorkingAtMonthOf(employeeWorkStart));
            }
        }

        TypedQuery<Company> query = createQueryWithSpecificationAndEntityGraph(spec, entityGraphName);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        //TODO: add sorting from pageable, but not implemented, because this solution causes
        //HHH000104: firstResult/maxResults specified with collection fetch; applying in memory!
        return query.getResultList();
    }

    @Transactional
    public Page<Company> findByExampleWithSpecificationPagedAndFetchAllRelationships(Company company, Pageable pageable) {
        Integer id = company.getId();
        String name = company.getName();

        Specification<Company> spec = Specification.where(null);
        if(id != null){
            spec = spec.and(idEquals(id));
        }

        if(StringUtils.hasLength(name)){
            spec = spec.and(nameContains(name));
        }

        if(!CollectionUtils.isEmpty(company.getEmployees())) {
            Employee employee = company.getEmployees().get(0);
            String employeeName = employee.getName();
            LocalDate employeeWorkStart = employee.getWorkStart();
            if(StringUtils.hasLength(employeeName)){
                spec = spec.and(hasEmployeeWithNamePrefix(employeeName));
            }
            if(employeeWorkStart != null){
                spec = spec.and(hasEmployeeStartedWorkingAtMonthOf(employeeWorkStart));
            }
        }

        Page<Company> page = companyRepository.findAll(spec, pageable);
        List<Integer> ids = page.getContent().stream().map(Company::getId).toList();
        companyRepository.findByIdInWithEmployees(ids);
        companyRepository.findByIdInWithAddresses(ids);

        return page;
    }

    @Transactional
    public Company addEmployee(int id, Employee employee) {
        Company company = companyRepository.findById(id).get();
        Employee savedEmployee = employeeRepository.save(employee);
        company.addEmployee(savedEmployee);
        company.getEmployees().size();
        company.getAddresses().size();
        return company;
    }
}
