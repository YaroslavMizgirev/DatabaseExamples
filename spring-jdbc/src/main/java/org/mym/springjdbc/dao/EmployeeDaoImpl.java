package org.mym.springjdbc.dao;

import org.mym.springjdbc.model.Employee;

import java.util.List;
import java.util.Optional;

public class EmployeeDaoImpl implements EmployeeDao {
    @Override
    public List<Employee> getAll() {
        return null;
    }

    @Override
    public Optional<Employee> getById(long id) {
        return Optional.empty();
    }

    @Override
    public void insert(Employee emp) {

    }

    @Override
    public void update(Employee emp) {

    }

    @Override
    public void delete(Employee emp) {

    }
}
