package com.tt.backend_challenge.service;

import com.tt.backend_challenge.enums.EmployeeState;
import com.tt.backend_challenge.enums.EventState;
import com.tt.backend_challenge.exceptions.DatabaseException;
import com.tt.backend_challenge.exceptions.EmployeeNotFoundException;
import com.tt.backend_challenge.exceptions.EventNotAcceptableException;
import com.tt.backend_challenge.machine.EmployeeStateMachine;
import com.tt.backend_challenge.model.Employee;
import com.tt.backend_challenge.model.dto.EmployeeDto;
import com.tt.backend_challenge.repository.EmployeeRepository;
import com.tt.backend_challenge.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeStateMachine employeeStateMachine;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

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
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        // Act
        Employee createdEmployee = employeeService.createEmployee(employeeDto);

        // Assert
        assertNotNull(createdEmployee);
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }


    @Test
    void testGetEmployeeById() {
        // Arrange
        Long employeeId = 1L;
        Employee employee = new Employee();
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        // Act
        Employee foundEmployee = employeeService.getEmployeeById(employeeId);

        // Assert
        assertNotNull(foundEmployee);
        assertEquals(employee, foundEmployee);
        verify(employeeRepository, times(1)).findById(employeeId);
    }

    @Test
    void testGetEmployeeByIdNotFound() {
        // Arrange
        Long employeeId = 1L;
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployeeById(employeeId));
    }

    @Test
    void testUpdateState_Success() {
        // Arrange
        Long employeeId = 1L;
        EventState state = EventState.BEGIN_CHECK;

        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setName("John Doe");
        employee.setAge(30);

        Set<EmployeeState> employeeStates = new HashSet<>(Arrays.asList(EmployeeState.IN_CHECK));

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(employeeStateMachine.getEmployeeStatesFromStateMachine(employee, state)).thenReturn(employeeStates);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        // Act
        Employee updatedEmployee = employeeService.updateState(employeeId, state);

        // Assert
        assertNotNull(updatedEmployee);
        assertEquals(employeeStates.size(), updatedEmployee.getStates().size());
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, times(1)).save(employee);
        verify(employeeStateMachine, times(1)).getEmployeeStatesFromStateMachine(employee, state);
    }

    @Test
    void testUpdateStateEmployeeNotFound() {
        // Arrange
        Long employeeId = 1L;
        EventState eventState = EventState.BEGIN_CHECK;
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.updateState(employeeId, eventState));
    }

    @Test
    void testUpdateStateEventNotAcceptable() {
        // Arrange
        Long employeeId = 1L;
        EventState eventState = EventState.ACTIVATE;
        Employee employee = new Employee();
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(employeeStateMachine.getEmployeeStatesFromStateMachine(employee, eventState))
                .thenThrow(new EventNotAcceptableException("event not acceptable"));

        // Act & Assert
        assertThrows(EventNotAcceptableException.class, () -> employeeService.updateState(employeeId, eventState));
    }

    @Test
    void testGetEmployeeByIdDatabaseException() {
        // Arrange
        Long employeeId = 1L;
        when(employeeRepository.findById(employeeId)).thenThrow(new DataAccessException("Database error") {});

        // Act & Assert
        assertThrows(DatabaseException.class, () -> employeeService.getEmployeeById(employeeId));
    }


    @Test
    void testDeleteEmployee() {
        // Arrange
        Long employeeId = 1L;
        when(employeeRepository.existsById(employeeId)).thenReturn(true);

        // Act
        employeeService.deleteEmployee(employeeId);

        // Assert
        verify(employeeRepository, times(1)).deleteById(employeeId);
    }

    @Test
    void testDeleteEmployeeNotFound() {
        // Arrange
        Long employeeId = 1L;
        when(employeeRepository.existsById(employeeId)).thenReturn(false);

        // Act & Assert
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.deleteEmployee(employeeId));
    }
}