package com.luv2code.springboot.thymeleafdemo.dao;

import com.luv2code.springboot.thymeleafdemo.entity.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public class EmployeeDao implements EmployeeRepository {
    private EntityManager entityManager;

    @Autowired
    public EmployeeDao(EntityManager theEntityManager){
        this.entityManager = theEntityManager;
    }

    @Override
    public List<Employee> findAllByOrderByLastNameAsc() {
        return List.of();
    }

    @Override
    public Employee findByUserName(String userName) {
        TypedQuery<Employee> theQuery = entityManager.createQuery("from Employee where userName = :uName and enabled = true", Employee.class);
        theQuery.setParameter("uName", userName);

        Employee theUser = null;
        try{
            theUser = theQuery.getSingleResult();
        }catch (Exception e){
            theUser = null;
        }
        return theUser;

    }

    @Override
    @Transactional
    public void save(Employee theEmployee) {
        entityManager.merge(theEmployee);
    }

    @Override
    public Optional<Employee> findById(int theId) {
        return Optional.empty();
    }

    @Override
    public void deleteById(int theId) {

    }
}
