package com.khb.hu.springcourse.hr.repository;

import com.khb.hu.springcourse.hr.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {
}
