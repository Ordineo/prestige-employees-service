package be.ordina.ordineo.controller;

import be.ordina.ordineo.model.Register;
import be.ordina.ordineo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
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
        service.initializeEmployee(register.getUsername(), passwordEncoder.encode(register.getPassword()));
        return ResponseEntity.noContent().build();
    }

}
