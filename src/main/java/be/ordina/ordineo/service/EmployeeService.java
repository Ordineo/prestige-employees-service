package be.ordina.ordineo.service;

import be.ordina.ordineo.Specification.EmployeeSpecificationsBuilder;
import be.ordina.ordineo.exception.EntityNotFoundException;
import be.ordina.ordineo.model.Employee;
import be.ordina.ordineo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
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
        Employee employeeBeforeUpdate = findByUsername(username);
        checkFieldsBeforeUpdate(employeeBeforeUpdate, employee);
        return save(employeeBeforeUpdate);
    }

    private Employee checkFieldsBeforeUpdate(Employee employeeBeforeUpdate, Employee employee) {

        if (employee.getUsername() != null) {
            employeeBeforeUpdate.setUsername(employee.getUsername());
        }
        if (employee.getAvatar() != null) {
            employeeBeforeUpdate.setAvatar(employee.getAvatar());
        }
        if (employee.getEmail() != null) {
            employeeBeforeUpdate.setEmail(employee.getEmail());
        }
        if (employee.getFirstName() != null) {
            employeeBeforeUpdate.setUsername(employee.getUsername());
        }
        if (employee.getGender() != null) {
            employeeBeforeUpdate.setGender(employee.getGender());
        }
        if (employee.getLastName() != null) {
            employeeBeforeUpdate.setLastName(employee.getLastName());
        }
        if (employee.getPassword() != null) {
            employeeBeforeUpdate.setPassword(employee.getPassword());
        }
        if (employee.getPhone() != null) {
            employeeBeforeUpdate.setPhone(employee.getPhone());
        }
        if (employee.getUnit() != null) {
            employeeBeforeUpdate.setUnit(employee.getUnit());
        }

        if (employee.getRoles().size() != employeeBeforeUpdate.getRoles().size()) {
            employeeBeforeUpdate.setRoles(employee.getRoles());
        } else {
            save(employeeBeforeUpdate);
        }
        return employeeBeforeUpdate;
    }

    public Employee findByUsername(String username) {
        Employee employee = employeeRepository.findByUsername(username);
        if (employee == null) {
            throw new EntityNotFoundException("Username does not exists!");
        }
        return employee;
    }
}
