package be.ordina.ordineo.service;

import be.ordina.ordineo.Specification.EmployeeSpecificationsBuilder;
import be.ordina.ordineo.exception.EntityNotFoundException;
import be.ordina.ordineo.model.Employee;
import be.ordina.ordineo.model.Role;
import be.ordina.ordineo.repository.EmployeeRepository;
import be.ordina.ordineo.repository.RoleRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, RoleRepository roleRepository) {
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
    }

    public Page<Employee> findAll(Optional<String> filter, Pageable pageable) {
        if (filter.isPresent()) {
            EmployeeSpecificationsBuilder employeeSpecificationsBuilder = new EmployeeSpecificationsBuilder();
            Pattern pattern = Pattern.compile("(\\w+?)(:)(\\w+?),");
            Matcher matcher = pattern.matcher(filter.get() + ",");
            while (matcher.find()) {
                employeeSpecificationsBuilder.with(matcher.group(1), matcher.group(2), matcher.group(3));
            }
            Specification<Employee> spec = employeeSpecificationsBuilder.build();
            return employeeRepository.findAll(spec, pageable);
        }

        return employeeRepository.findAll(pageable);
    }

    @Transactional
    public void delete(String username) {
        final Employee employee = findByUsername(username);
        employee.setEnabled(0);
        employee.setDeleted(1);
        employeeRepository.save(employee);
    }

    @Transactional
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Transactional
    public Employee update(String username, Employee employee) {
        final Employee byUsername = findByUsername(username);
        Hibernate.initialize(employee.getRoles());
        employee.setUuid(byUsername.getUuid());
        employee.setRoles(byUsername.getRoles());

        return employeeRepository.save(employee);
    }

    public Employee findByUsername(String username) {
        Employee employee = employeeRepository.findByUsername(username);
        if (employee == null) {
            throw new EntityNotFoundException("Username does not exists!");
        }
        return employee;
    }

    @Transactional
    public void initializeEmployee(String username, String password) {
        final Role admin = roleRepository.findByTitle("admin");
        final Employee employee = findByUsername(username);
        employee.setEnabled(1);
        employee.setPassword(password);
        employee.setRoles(new ArrayList<>(Collections.singletonList(admin)));
        employeeRepository.save(employee);
    }
}
