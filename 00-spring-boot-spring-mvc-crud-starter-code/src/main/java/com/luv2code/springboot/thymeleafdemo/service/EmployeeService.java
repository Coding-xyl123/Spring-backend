package com.luv2code.springboot.thymeleafdemo.service;

import com.luv2code.springboot.thymeleafdemo.entity.Employee;
import com.luv2code.springboot.thymeleafdemo.user.WebUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface EmployeeService extends UserDetailsService {

    List<Employee> findAll();

    Employee findById(int theId);

    void save(WebUser webUser);

    void deleteById(int theId);

    public Employee findByUserName(String userName);

    Employee save(Employee theEmployee);
}
