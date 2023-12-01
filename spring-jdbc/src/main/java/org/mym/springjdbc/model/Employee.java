package org.mym.springjdbc.model;

import lombok.Data;

@Data
public class Employee {
    private final long id;
    private final String firstName;
    private final String lastName;
    private final String department;
    private final double salary;
}
