package net.javaguides.springboot.controller;

import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    //get employees
    @GetMapping("employees")
    private List<Employee> getEmployees() {
        return this.employeeRepository.findAll();
    }

    //get employee
    @GetMapping("employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "id") Long employeeId)
            throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));
        return ResponseEntity.ok().body(employee);
    }

    //add employee
    @PostMapping("employees")
    public Employee addEmployee(@RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }

    //update employee
    @PutMapping("employees/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @Validated @RequestBody Employee employeeDetails)
            throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + id));
        employee.setEmail(employeeDetails.getEmail());
        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());

        return ResponseEntity.ok(employeeRepository.save(employee));
    }

    //delete employee
    @DeleteMapping("employees/{id}")
    public Map<String, Boolean> deleteEmployee(@PathVariable Long id)
            throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + id));

        employeeRepository.delete(employee);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);

        return response;
    }
}
