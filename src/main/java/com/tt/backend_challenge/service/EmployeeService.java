package com.tt.backend_challenge.service;

import com.tt.backend_challenge.enums.EmployeeState;
import com.tt.backend_challenge.enums.EventState;
import com.tt.backend_challenge.model.Employee;
import com.tt.backend_challenge.model.dto.EmployeeDto;

import java.util.List;

public interface EmployeeService {
    public Employee createEmployee(EmployeeDto employeeDto);
    public Employee updateState(Long id, EventState event);
    public Employee getEmployeeById(Long id);
    public List<Employee> getAllEmployees();

}
