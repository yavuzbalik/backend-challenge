package com.tt.backend_challenge.service.impl;

import com.tt.backend_challenge.enums.EmployeeState;
import com.tt.backend_challenge.enums.EventState;
import com.tt.backend_challenge.exceptions.DatabaseException;
import com.tt.backend_challenge.exceptions.EmployeeNotFoundException;
import com.tt.backend_challenge.exceptions.EventNotAcceptableException;
import com.tt.backend_challenge.machine.EmployeeStateMachine;
import com.tt.backend_challenge.model.Employee;
import com.tt.backend_challenge.model.dto.EmployeeDto;
import com.tt.backend_challenge.repository.EmployeeRepository;
import com.tt.backend_challenge.service.EmployeeService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeStateMachine employeeStateMachine;

    @Transactional
    public Employee createEmployee(EmployeeDto employeeDto) {
        Employee employee = new Employee();
        employee.setName(employeeDto.getName());
        employee.setAge(employeeDto.getAge());
        return employeeRepository.save(employee);
    }

    @Transactional
    public Employee updateState(Long employeeId, EventState state) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + employeeId));
        try {
            Collection<EmployeeState> employeeStates = employeeStateMachine.getEmployeeStatesFromStateMachine(employee, state);
            employee.setStates(new LinkedHashSet<>(employeeStates));
            return employeeRepository.save(employee);
        }
        catch (EventNotAcceptableException e){
            throw new EventNotAcceptableException("event not acceptable for employee id = "+e);
        }
    }

    public Employee getEmployeeById(Long employeeId) {
        try {
            return employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + employeeId));
        } catch (DataAccessException ex) {
            throw new DatabaseException("Database error occurred", ex);
        }
    }

    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    public void deleteEmployee(Long employeeId){
        employeeRepository.deleteById(employeeId);
    }
}
