package com.khb.hu.springcourse.hr.repository;

import com.khb.hu.springcourse.hr.model.Employee;
import com.khb.hu.springcourse.hr.model.SalaryStat;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    List<Employee> findByJobStartingWithIgnoreCase(String jobName);

    @Query("SELECT e FROM Employee e WHERE e.id=:id")
    @EntityGraph(attributePaths = "company.addresses")
    Optional<Employee> findByIdWithCompanyAndAddresses(int id);

    @Query("SELECT e.company.id AS companyId, e.job AS job, avg(e.salary) AS averageSalary " +
            "FROM Employee e " +
            "GROUP BY e.company.id, e.job")
    List<SalaryStat> findAverageSalaries();
}
