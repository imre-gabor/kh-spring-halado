package com.khb.hu.springcourse.hr.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.*;

@Entity
@NamedEntityGraph(
        name = "Company.withEmployees",
        attributeNodes = {
            @NamedAttributeNode("employees")
        }
)
@NamedEntityGraph(
        name = "Company.withEmployeesAndAddresses",
        attributeNodes = {
                @NamedAttributeNode("employees"),
                @NamedAttributeNode("addresses")
        }
)
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @OneToMany(mappedBy = "company"/*, fetch = FetchType.EAGER*/)
    //@Fetch(FetchMode.SELECT)
    private List<Employee> employees;

    @ManyToMany(mappedBy = "companies")
    private Set<Address> addresses;

    public Company(){}

    public Company(String name) {
        this(null, name);
    }

    public Company(Integer id) {
        this(id, null);
    }

    public Company(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addEmployee(Employee employee){
        employee.setCompany(this);
        if(this.employees == null)
            this.employees = new ArrayList<>();
        this.employees.add(employee);
    }

    public void addAddress(Address address){
        address.getCompanies().add(this);
        if(this.addresses == null)
            this.addresses = new HashSet<>();
        this.addresses.add(address);
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Employee> getEmployees() {
        return employees == null ? null : Collections.unmodifiableList(employees);
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public Set<Address> getAddresses() {
        if(this.addresses == null)
            this.addresses = new HashSet<>();
        return addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return Objects.equals(id, company.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
