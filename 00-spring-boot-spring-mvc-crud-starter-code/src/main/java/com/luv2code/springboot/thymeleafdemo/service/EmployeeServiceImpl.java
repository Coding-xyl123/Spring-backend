package com.luv2code.springboot.thymeleafdemo.service;

import com.luv2code.springboot.thymeleafdemo.dao.EmployeeRepository;
import com.luv2code.springboot.thymeleafdemo.dao.RoleDao;
import com.luv2code.springboot.thymeleafdemo.entity.Employee;
import com.luv2code.springboot.thymeleafdemo.entity.Role;
import com.luv2code.springboot.thymeleafdemo.user.WebUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;
    private RoleDao roleDao;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, RoleDao roleDao, BCryptPasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAllByOrderByLastNameAsc();
    }

    @Override
    public Employee findById(int theId) {
        Optional<Employee> result = employeeRepository.findById(theId);

        Employee theEmployee = null;

        if (result.isPresent()) {
            theEmployee = result.get();
        }
        else {
            // we didn't find the employee
            throw new RuntimeException("Did not find employee id - " + theId);
        }

        return theEmployee;
    }

    @Override
    public void save(WebUser webUser) {
        Employee employee = new Employee();

        employee.setUserName(webUser.getUserName());
        employee.setPassword(passwordEncoder.encode(webUser.getPassword()));
        employee.setFirstName(webUser.getFirstName());
        employee.setLastName(webUser.getLastName());
        employee.setEmail(webUser.getEmail());
        employee.setEnabled(true);

        employee.setRoles(Arrays.asList(roleDao.findRoleByName("ROLE_EMPLOYEE")));
        employeeRepository.save(employee);
    }

    @Override
    public void deleteById(int theId) {
        employeeRepository.deleteById(theId);
    }

    @Override
    public Employee findByUserName(String userName) {
        return employeeRepository.findByUserName(userName);
    }

    @Override
    public Employee save(Employee theEmployee) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByUserName(username);
        if(employee == null){
            throw new UsernameNotFoundException("Invalid username or password");
        }
        Collection<SimpleGrantedAuthority> authorities = mapRolesToAuthorities(employee.getRoles());
        return new org.springframework.security.core.userdetails.User(employee.getUserName(), employee.getPassword(), authorities
        );
    }

    private Collection<SimpleGrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for(Role tempRole : roles){
            SimpleGrantedAuthority  tempAuthority = new SimpleGrantedAuthority(tempRole.getName());
            authorities.add(tempAuthority);
        }
        return authorities;
    }
}






