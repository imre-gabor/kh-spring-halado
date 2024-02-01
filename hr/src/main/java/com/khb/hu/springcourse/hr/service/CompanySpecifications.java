package com.khb.hu.springcourse.hr.service;

import com.khb.hu.springcourse.hr.model.Company;
import com.khb.hu.springcourse.hr.model.Company_;
import com.khb.hu.springcourse.hr.model.Employee_;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class CompanySpecifications {

    public static Specification<Company> idEquals(Integer id){
        return (root, cq, cb) -> cb.equal(root.get(Company_.id), id);
    }

    public static Specification<Company> nameContains(String name){
        return (root, cq, cb) -> cb.like(root.get(Company_.name), "%" + name + "%");
    }

    public static Specification<Company> hasEmployeeWithNamePrefix(String namePrefix){
        return (root, cq, cb) -> cb.like(root.join(Company_.employees).get(Employee_.name), namePrefix + "%");
    }

    public static Specification<Company> hasEmployeeStartedWorkingAtMonthOf(LocalDate date){
        return (root, cq, cb) -> cb.between(root.join(Company_.employees).get(Employee_.workStart),
                date.withDayOfMonth(1), getLastDayOfMonth(date));
    }

    public static LocalDate getLastDayOfMonth(LocalDate date) {
        return date.withDayOfMonth(date.getMonth().length(date.isLeapYear()));
    }

}
