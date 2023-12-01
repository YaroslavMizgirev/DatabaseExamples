package org.mym.dao;

import org.junit.jupiter.api.*;
import org.mym.model.Employee;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DAO must be able to do:")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EmployeeJdbcDAOTest {
    private EmployeeJdbcDAO dao;

    @BeforeAll
    void beforeAll() {
        dao = new EmployeeJdbcDAO();
    }

    @DisplayName("The expected number of employees should return")
    @Test
    void expectedNumbersOfEmployeesShouldReturn() {
        assertThat(dao.count()).isEqualTo(10);
    }

    @DisplayName("The expected and actual Employees list contains exactly same values in any order")
    @Test
    void expectedEmployeesListShouldReturn() {
        assertThat(dao.getAll())
                .hasSameElementsAs(new ArrayList<>() {{
                    add(Employee.builder().id(1).firstname("Варлам").lastname("Дементьев").department("Кинезитерапевт").salary(54922.77d).build());
                    add(Employee.builder().id(8).firstname("Мартын").lastname("Филиппов").department("Шахтёр").salary(7274.51d).build());
                    add(Employee.builder().id(9).firstname("Герман").lastname("Кузьмин").department("Стоматолог").salary(6515.56d).build());
                    add(Employee.builder().id(2).firstname("Карл").lastname("Мышкин").department("Фермер").salary(54922.77d).build());
                    add(Employee.builder().id(3).firstname("Мечеслав").lastname("Веселов").department("Полицейский").salary(39936.34d).build());
                    add(Employee.builder().id(4).firstname("Митрофан").lastname("Фомин").department("Судебный пристав").salary(13764.94d).build());
                    add(Employee.builder().id(5).firstname("Владимир").lastname("Носков").department("Табаковод").salary(26675.34d).build());
                    add(Employee.builder().id(6).firstname("Феликс").lastname("Васильев").department("Дерматолог").salary(12420.93d).build());
                    add(Employee.builder().id(7).firstname("Абрам").lastname("Логинов").department("Борт-радист").salary(7010.43d).build());
                    add(Employee.builder().id(10).firstname("Альфред").lastname("Зуев").department("Журналист").salary(6316.91d).build());
                }});
    }

    @DisplayName("The expected and actual Employee is equal")
    @Test
    void expectedEmployeeByIdShouldReturn() {
        assertThat(dao.get(1).orElseThrow()).isEqualTo(Optional.of(Employee.builder()
                        .id(1).firstname("Варлам").lastname("Дементьев").department("Кинезитерапевт").salary(54922.77d).build())
                .get());
    }

    @DisplayName("The expected Employee should insert, update, delete")
    @Test
    void expectedEmployeeShouldInsertUpdateDelete() {
        Employee employee = Employee.builder().id(11).firstname("Yaroslav").lastname("Mizgirev").department("IT").salary(1000000.00d).build();
        assertThat(dao.insert(employee)).isTrue();
        assertThat(dao.get(employee.getFirstname(), employee.getLastname(), employee.getDepartment(), employee.getSalary()))
                .hasSameElementsAs(new ArrayList<>() {{ add(employee); }});

        Employee newEmployee = Employee.builder().id(11).firstname("Anna").lastname("Mizgireva").department("Housewife").salary(500000.00d).build();
        assertThat(dao.update(employee,newEmployee.getFirstname(),newEmployee.getLastname(),newEmployee.getDepartment(),newEmployee.getSalary())).isTrue();
        assertThat(dao.get(newEmployee.getFirstname(), newEmployee.getLastname(), newEmployee.getDepartment(), newEmployee.getSalary()))
                .hasSameElementsAs(new ArrayList<>() {{ add(newEmployee); }});

        assertThat(dao.delete(newEmployee)).isTrue();
    }

    @AfterAll
    void afterAll() {
        dao.close();
    }
}