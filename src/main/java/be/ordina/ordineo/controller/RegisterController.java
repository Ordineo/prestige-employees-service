package be.ordina.ordineo.controller;

import be.ordina.ordineo.model.Employee;
import be.ordina.ordineo.model.Role;
import be.ordina.ordineo.repository.EmployeeRepository;
import be.ordina.ordineo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by SaFu on 19/04/2017.
 */
@RestController
public class RegisterController {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public void enableAccount(@RequestParam(name = "username") String username, @RequestParam(name = "password") String password) {
        Employee employee = employeeRepository.findByUsername(username);
        employee.setPassword(bCryptPasswordEncoder.encode(password));
        employee.setEnabled(1);
        Role userRole = roleRepository.findByTitle("admin");
        employee.setRoles((List<Role>) new HashSet<Role>(Arrays.asList(userRole)));
        employeeRepository.save(employee);
    }

}
