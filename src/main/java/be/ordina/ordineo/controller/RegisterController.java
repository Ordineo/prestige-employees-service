package be.ordina.ordineo.controller;

import be.ordina.ordineo.model.Employee;
import be.ordina.ordineo.model.Register;
import be.ordina.ordineo.service.EmployeeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/register",
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE},
        consumes = {MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE})
public class RegisterController {

    private final EmployeeService service;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public RegisterController(EmployeeService employeeService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.service = employeeService;
        this.passwordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("")
    public ResponseEntity enableAccount(@RequestBody Register register) {
        if (!register.getPassword().equals(register.getConfirmPassword())) {
            return new ResponseEntity<>("Password and confirm password not the same", HttpStatus.BAD_REQUEST);
        }
        Employee employee = service.findByUsername(register.getUsername());
        if (employee.getEnabled() == 1 || StringUtils.isNotBlank(employee.getPassword())) {
            return new ResponseEntity<>("Account is already active", HttpStatus.BAD_REQUEST);
        }
        service.initializeEmployee(register.getUsername(), passwordEncoder.encode(register.getPassword()));
        return ResponseEntity.noContent().build();
    }

}
