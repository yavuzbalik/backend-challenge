package com.tt.backend_challenge.controller;

import com.tt.backend_challenge.enums.EmployeeState;
import com.tt.backend_challenge.enums.EventState;
import com.tt.backend_challenge.model.Employee;
import com.tt.backend_challenge.model.dto.EmployeeDto;
import com.tt.backend_challenge.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public Employee createEmployee(@RequestBody EmployeeDto employeeDto) {
        return employeeService.createEmployee(employeeDto);
    }

    @PatchMapping("/{id}/state")
    public Employee updateState(@PathVariable Long id, @RequestParam EventState state) {
        return employeeService.updateState(id, state);
    }

    @GetMapping("/{id}")
    public Employee getEmployee(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    @GetMapping()
    public List<Employee> getAllEmployees(){return employeeService.getAllEmployees();}
}

