package com.tt.backend_challenge.model;

import com.tt.backend_challenge.enums.EmployeeState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "employee")
@Getter
@Setter
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private int age;

    @ElementCollection
    private Set<EmployeeState> states = new LinkedHashSet<>();

    public Employee() {
        states.add(EmployeeState.ADDED);
    }
}

