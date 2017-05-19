package be.ordina.ordineo.controller;

import be.ordina.ordineo.model.Employee;
import be.ordina.ordineo.model.Role;
import be.ordina.ordineo.repository.EmployeeRepository;
import be.ordina.ordineo.repository.RoleRepository;
import be.ordina.ordineo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
        System.out.println(employee);
        employee.setPassword(bCryptPasswordEncoder.encode(password));
        employee.setEnabled(1);
        Role userRole = roleRepository.findByTitle("admin");
        System.out.println(userRole);
        // Arrays.asList
        // Collections.sign...
        List<Role> roles = new ArrayList<>();
        roles.add(userRole);
        employee.setRoles(roles);
        System.out.println(employee);
        employeeRepository.save(employee);
    }

}
