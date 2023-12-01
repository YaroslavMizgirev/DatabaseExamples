package org.mym.dao;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.mym.Main;
import org.mym.model.Employee;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

@Slf4j
public class EmployeeJdbcDAO implements DAO<Employee> {
    private Properties properties = new Properties();
    private Connection connection;

    public EmployeeJdbcDAO() {
        try {
            setProperties("app.properties");
            this.connection = DriverManager.getConnection(properties.getProperty("database.mysql.url"));
            log.info(String.format("Connection open with database: %s, with user: %s",
                    this.connection.getMetaData().getDatabaseProductName(),
                    this.connection.getMetaData().getUserName()));
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    void close() {
        try {
            if (connection.isValid(0)) {
                connection.close();
                log.info("Connection close");
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    public void setProperties(@NonNull String fileName) {
        try (InputStream input = new FileInputStream(
                Objects.requireNonNull(
                        Main.class.getClassLoader().getResource(fileName)).getPath())) {
            this.properties.load(input);
        } catch (IOException ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Employee> getAll() {
        try (Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery("select * from employees")) {
            List<Employee> list = new ArrayList<>();
            while (result.next()) {
                list.add(
                        Employee.builder()
                                .id(result.getInt("id"))
                                .firstname(result.getString("firstname"))
                                .lastname(result.getString("lastname"))
                                .department(result.getString("department"))
                                .salary(result.getDouble("salary"))
                                .build());
            }
            log.info(String.format("Get all: %s records of employees", list));
            return list;
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Optional<Employee> get(@NonNull int id) {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(String.format("select * from employees where id=%d", id))) {
            Optional<Employee> employee = Optional.empty();
            if (resultSet.next()) {
                employee = Optional.of(Employee.builder()
                        .id(resultSet.getInt("id"))
                        .firstname(resultSet.getString("firstname"))
                        .lastname(resultSet.getString("lastname"))
                        .department(resultSet.getString("department"))
                        .salary(resultSet.getDouble("salary"))
                        .build());
            }
            log.info(String.format("Get by id: %s", employee.orElseThrow()));
            return employee;
        } catch (SQLException | NoSuchElementException ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Employee> get(Object... params) {
        String sqlQuery = getSelectString(params);
        try (Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(sqlQuery)) {
            List<Employee> list = new ArrayList<>();
            while (result.next()) {
                list.add(
                        Employee.builder()
                                .id(result.getInt("id"))
                                .firstname(result.getString("firstname"))
                                .lastname(result.getString("lastname"))
                                .department(result.getString("department"))
                                .salary(result.getDouble("salary"))
                                .build());
            }
            log.info(String.format("Success select with conditions %s: %s", Arrays.toString(params), list));
            return list;
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    private String getSelectString(Object... params) {
        String condition = "";
        if (params.length > 0 && params[0] instanceof String && !((String) params[0]).isEmpty() && !((String) params[0]).isBlank()) {
            condition = String.format(Locale.US,"firstname=\"%s\"", ((String) params[0]));
        }
        if (params.length > 1 && params[1] instanceof String && !((String) params[1]).isEmpty() && !((String) params[1]).isBlank()) {
            condition = String.format(Locale.US,"%s and lastname=\"%s\"", condition, ((String) params[1]));
        }
        if (params.length > 2 && params[2] instanceof String && !((String) params[2]).isEmpty() && !((String) params[2]).isBlank()) {
            condition = String.format(Locale.US,"%s and department=\"%s\"", condition, ((String) params[2]));
        }
        if (params.length > 3 && params[3] instanceof Double && ((Double) params[3]) != 0d) {
            condition = String.format(Locale.US,"%s and salary=%s", condition, ((Double) params[3]));
        }

        return String.format(Locale.US,"select * from employees where (%s)", condition);
    }

    private String getUpdateString(int id, Object... params) {
        String condition = "";
        if (params.length > 0 && params[0] instanceof String && !((String) params[0]).isEmpty() && !((String) params[0]).isBlank()) {
            condition = String.format(Locale.US,"firstname=\"%s\"", ((String) params[0]));
        }
        if (params.length > 1 && params[1] instanceof String && !((String) params[1]).isEmpty() && !((String) params[1]).isBlank()) {
            condition = String.format(Locale.US,"%s, lastname=\"%s\"", condition, ((String) params[1]));
        }
        if (params.length > 2 && params[2] instanceof String && !((String) params[2]).isEmpty() && !((String) params[2]).isBlank()) {
            condition = String.format(Locale.US,"%s, department=\"%s\"", condition, ((String) params[2]));
        }
        if (params.length > 3 && params[3] instanceof Double && ((Double) params[3]) != 0d) {
            condition = String.format(Locale.US,"%s, salary=%.2f", condition, ((Double) params[3]));
        }

        return String.format(Locale.US,"update employees set %s where id=%d", condition, id);
    }

    private String getInsertString(Employee employee) {
        String values = "";
        values = String.format(Locale.US, "%d,\"%s\",\"%s\",\"%s\",%.2f",
                employee.getId(),
                employee.getFirstname(),
                employee.getLastname(),
                employee.getDepartment(),
                employee.getSalary());

        return String.format(Locale.US,"insert into employees (id,firstname,lastname,department,salary) values (%s)", values);
    }

    @Override
    public boolean insert(@NonNull Employee employee) {
        String sqlQuery = getInsertString(employee);
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            int i = statement.executeUpdate();
            log.info(String.format("Success insert: %s with result %d", employee, i));
            return true;
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(@NonNull Employee employee, Object... params) {
        String sqlQuery = getUpdateString(employee.getId(), params);
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            int i = statement.executeUpdate();
            log.info(String.format("Success update: %s with result %d", employee, i));
            return true;
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(@NonNull Employee employee) {
        try (PreparedStatement statement = connection.prepareStatement("delete from employees where (id=?)")) {
            statement.setInt(1, employee.getId());
            int i = statement.executeUpdate();
            log.info(String.format("Success delete: %s with result %d", employee, i));
            return true;
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            return false;
        }
    }

    @Override
    public int count() {
        try (Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery("select count(*) from employees")) {
            int numbersOfEmployees = 0;
            if (result.next()) {
                numbersOfEmployees = result.getInt(1);
            }
            log.info(String.format("count: %d records of employees", numbersOfEmployees));
            return numbersOfEmployees;
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}
