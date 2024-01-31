package com.khb.hu.springcourse.hr.repository;

import com.khb.hu.springcourse.hr.model.Company;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Integer>, JpaSpecificationExecutor<Company> {

    @Query("SELECT c FROM Company c WHERE c.id=:id")
    @EntityGraph(attributePaths = "employees")
    Optional<Company> findByIdWithEmployees(Integer id);

    @Query("SELECT c FROM Company c WHERE c.id IN :ids")
    @EntityGraph(attributePaths = "employees")
    List<Company> findByIdInWithEmployees(Collection<Integer> ids);

    @Query("SELECT c FROM Company c WHERE c.id IN :ids")
    @EntityGraph(attributePaths = "addresses")
    List<Company> findByIdInWithAddresses(Collection<Integer> ids);
}
