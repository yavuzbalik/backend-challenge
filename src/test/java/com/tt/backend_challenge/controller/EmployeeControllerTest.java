package com.tt.backend_challenge.controller;

import com.tt.backend_challenge.enums.EventState;
import com.tt.backend_challenge.model.Employee;
import com.tt.backend_challenge.model.dto.EmployeeDto;
import com.tt.backend_challenge.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateEmployee() {
        // Arrange
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setName("John Doe");
        employeeDto.setAge(30);
        Employee employee = new Employee();
        when(employeeService.createEmployee(employeeDto)).thenReturn(employee);

        // Act
        ResponseEntity<Employee> response = employeeController.createEmployee(employeeDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(employee, response.getBody());
        verify(employeeService, times(1)).createEmployee(employeeDto);
    }

    @Test
    void testUpdateState() {
        // Arrange
        Long employeeId = 1L;
        EventState state = EventState.BEGIN_CHECK;
        Employee updatedEmployee = new Employee();
        when(employeeService.updateState(employeeId, state)).thenReturn(updatedEmployee);

        // Act
        ResponseEntity<Employee> response = employeeController.updateState(employeeId, state);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedEmployee, response.getBody());
        verify(employeeService, times(1)).updateState(employeeId, state);
    }

    @Test
    void testGetEmployee() {
        // Arrange
        Long employeeId = 1L;
        Employee employee = new Employee();
        when(employeeService.getEmployeeById(employeeId)).thenReturn(employee);

        // Act
        ResponseEntity<Employee> response = employeeController.getEmployee(employeeId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employee, response.getBody());
        verify(employeeService, times(1)).getEmployeeById(employeeId);
    }

    @Test
    void testGetAllEmployees() {
        // Arrange
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(employeeService.getAllEmployees()).thenReturn(employees);

        // Act
        ResponseEntity<List<Employee>> response = employeeController.getAllEmployees();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employees, response.getBody());
        verify(employeeService, times(1)).getAllEmployees();
    }
}
