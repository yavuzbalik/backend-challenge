package com.tt.backend_challenge.repository;

import com.tt.backend_challenge.enums.EmployeeState;
import com.tt.backend_challenge.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}

