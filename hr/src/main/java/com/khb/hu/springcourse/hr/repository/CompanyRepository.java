package com.khb.hu.springcourse.hr.repository;

import com.khb.hu.springcourse.hr.model.Company;
import com.khb.hu.springcourse.hr.model.QCompany;
import com.khb.hu.springcourse.hr.service.CompanySpecifications;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Integer>,
        JpaSpecificationExecutor<Company>,
        QuerydslPredicateExecutor<Company>,
        QuerydslBinderCustomizer<QCompany> {

    @Query("SELECT c FROM Company c WHERE c.id=:id")
    @EntityGraph(attributePaths = "employees")
    Optional<Company> findByIdWithEmployees(Integer id);

    @Query("SELECT c FROM Company c WHERE c.id IN :ids")
    @EntityGraph(attributePaths = "employees")
    List<Company> findByIdInWithEmployees(Collection<Integer> ids);

    @Query("SELECT c FROM Company c WHERE c.id IN :ids")
    @EntityGraph(attributePaths = "addresses")
    List<Company> findByIdInWithAddresses(Collection<Integer> ids);

    @Override
    default void customize(QuerydslBindings bindings, QCompany company){
        bindings.bind(company.name).first((path, value) -> path.contains(value));
        bindings.bind(company.employees.any().name).first(((path, value) -> path.startsWith(value)));
        bindings.bind(company.employees.any().workStart).first((path, value)
                -> path.between(value.withDayOfMonth(1), CompanySpecifications.getLastDayOfMonth(value)));

    }
}
