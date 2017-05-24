package be.ordina.ordineo.service;

import be.ordina.ordineo.Specification.EmployeeSpecificationsBuilder;
import be.ordina.ordineo.exception.EntityNotFoundException;
import be.ordina.ordineo.model.Employee;
import be.ordina.ordineo.model.Role;
import be.ordina.ordineo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shbe on 09/05/2017.
 */
@Service
public class EmployeeService {//implements BaseService<Employee> {


    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private RoleService roleService;

   /* @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, RoleService roleService) {
        this.employeeRepository = employeeRepository;
        this.roleService = roleService;
    }*/


    public Page<Employee> findAll(Optional<String> filter, Pageable pageable) {
        if (filter.isPresent()) {
            return applyFilter(filter, pageable);
        }

        return employeeRepository.findAll(pageable);
    }

    private Page<Employee> applyFilter(Optional<String> filter, Pageable pageable) {
        EmployeeSpecificationsBuilder employeeSpecificationsBuilder = new EmployeeSpecificationsBuilder();
        StringBuilder concatenateFilterWithCommaSeparator = new StringBuilder();
        if (filter.isPresent()) {
            concatenateFilterWithCommaSeparator.append(filter.get().concat(","));
        }
        Pattern pattern = Pattern.compile("(\\w+?)(:)(\\w+?),");
        Matcher matcher = pattern.matcher(concatenateFilterWithCommaSeparator);
        while (matcher.find()) {
            employeeSpecificationsBuilder.with(matcher.group(1), matcher.group(2), matcher.group(3));
        }
        Specification<Employee> spec = employeeSpecificationsBuilder.build();
        return employeeRepository.findAll(spec, pageable);
    }

    @Transactional
    public void delete(String username) throws EntityNotFoundException{
        Employee employee = employeeRepository.findByUsername(username);
        if(employee == null){
            throw new EntityNotFoundException("Username does not exist!");
        }
        if (!employee.getRoles().isEmpty()) {
            unlinkRole(employee);
        }
        employeeRepository.delete(employee.getUuid());
    }

    @Transactional
    private void unlinkRole(Employee employee) {
        List<Role> roles = employee.getRoles();
        for (Role role : roles) {

            if (role.getEmployees().contains(employee)) {
                role.getEmployees().remove(employee);
            }

        }
        employee.getRoles().removeAll(roles);
        roleService.saveAll(roles);
    }

    @Transactional
    public Employee save(Employee employee) {
        if (!employee.getRoles().isEmpty()) {
            return saveWithRoles(employee);
        }
        return employeeRepository.save(employee);
    }

    @Transactional
    private Employee saveWithRoles(Employee employee) {
        List<Role> roles = employee.getRoles();
        for (Role role : roles) {
            Role existingRole = roleService.findByTitle(role.getTitle());
            if (existingRole == null) {
                throw new EntityNotFoundException("Role does no exist!");
            }
            role.setId(existingRole.getId());
        }
        employee.setRoles(roles);
        return employeeRepository.save(employee);
    }

    @Transactional
    public List<Employee> saveAll(List<Employee> employees) {
        return employeeRepository.save(employees);
    }

    @Transactional
    public Employee update(String username, Employee employee) throws EntityNotFoundException{
        //Employee employeeBeforeUpdate = findByUsername(username);
        Employee employeeBeforeUpdate = employeeRepository.findByUsername(username);
        if(employeeBeforeUpdate == null){
            throw new EntityNotFoundException("Username does not exist!");
        }
        checkFieldsBeforeUpdate(employeeBeforeUpdate, employee);
        return save(employeeBeforeUpdate);

    }

    private Employee checkFieldsBeforeUpdate(Employee employeeBeforeUpdate, Employee employee) {
        if(!employee.getRoles().isEmpty()){
            employeeBeforeUpdate.setRoles(employee.getRoles());
        }
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

    return employeeBeforeUpdate;
}

    public Page<Employee> findByRole(String roleTitle) throws EntityNotFoundException{
        Role role = roleService.findByTitle(roleTitle);
        if (role == null) {
            throw new EntityNotFoundException("Role does not exist!");
        }
        return new PageImpl<Employee>(role.getEmployees());
    }

    public Employee findByUsername(String username) throws  EntityNotFoundException{
        Employee employee = employeeRepository.findByUsername(username);
        if (employee == null) {
            throw new EntityNotFoundException("Username does not exist!");
        }
        return employee;
    }

}


