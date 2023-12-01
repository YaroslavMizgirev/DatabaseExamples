package org.mym.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Employee {
    private final int id;
    private final String firstname;
    private final String lastname;
    private final String department;
    private final double salary;
}
