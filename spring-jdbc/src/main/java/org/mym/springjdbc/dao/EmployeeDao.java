package org.mym.springjdbc.dao;

import org.mym.springjdbc.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeDao {
    List<Employee> getAll();
    Optional<Employee> getById(long id);
    void insert(Employee emp);
    void update(Employee emp);
    void delete (Employee emp);
}
