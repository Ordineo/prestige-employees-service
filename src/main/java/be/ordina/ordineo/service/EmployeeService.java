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
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shbe on 13/04/2017.
 */
@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private RoleService roleService;

    /*@Autowired
    public EmployeeService(EmployeeRepository employeeRepository, RoleService roleService) {
        this.employeeRepository = employeeRepository;
        this.roleService = roleService;
    }*/

    public Page<Employee> findAll(Optional<String> filter , Pageable pageable){
        if(filter.isPresent()){
            EmployeeSpecificationsBuilder employeeSpecificationsBuilder = new EmployeeSpecificationsBuilder();
            Pattern pattern = Pattern.compile("(\\w+?)(:)(\\w+?),");
            Matcher matcher = pattern.matcher(filter.get() + ",");
            while (matcher.find()) {
                employeeSpecificationsBuilder.with(matcher.group(1), matcher.group(2), matcher.group(3));
            }
            Specification<Employee> spec = employeeSpecificationsBuilder.build();
            return employeeRepository.findAll(spec,pageable);
        }

        return employeeRepository.findAll(pageable);
    }

    public Employee findByUUId(UUID uuid){
        Employee employee = employeeRepository.findByUuid(uuid);
        if( employee == null){
            throw new EntityNotFoundException("Employee does not exist!");
        }
         return employee;
    }

    @Transactional
    public void delete(String username){
        final Employee employee = findByUsername(username);
        employee.setEnabled(0);
        employee.setDeleted(1);
        employeeRepository.save(employee);
    }

    @Transactional
    private void unlinkRole(Employee employee) {
        List<Role> roles = employee.getRoles();
        for(Role role :roles){

                    if(role.getEmployees().contains(employee)){
                        role.getEmployees().remove(employee);
                    }

        }
        employee.getRoles().removeAll(roles);
        roleService.saveAll(roles);
    }

    @Transactional
    public Employee save(Employee employee){
       return employeeRepository.save(employee);
    }
    @Transactional
    public Employee saveWithRoles(Employee employee) {
        List<Role> roles = employee.getRoles();
        for(Role role : roles ){
            Role existingRole = roleService.findByTitle(role.getTitle());
            if ( existingRole == null){
                throw new EntityNotFoundException("Role does no exist!");
            }
            role.setId(existingRole.getId());
        }
        employee.setRoles(roles);
        return save(employee);
    }

    @Transactional
    public List<Employee> saveAll(List<Employee> employees){
        return employeeRepository.save(employees);
    }

    @Transactional
    public Employee update(String username , Employee employee){
        Employee employeeBeforeUpdate = findByUsername(username);//employeeRepository.findByUuid(uuid); ???? question 1: --->this is better
        checkFieldsBeforeUpdate(employeeBeforeUpdate,employee);
        return save(employeeBeforeUpdate);

    }

    private Employee  checkFieldsBeforeUpdate(Employee employeeBeforeUpdate, Employee employee) {

        if(employee.getUsername() !=null){
            employeeBeforeUpdate.setUsername(employee.getUsername());
        }
        if(employee.getAvatar()!=null){
            employeeBeforeUpdate.setAvatar(employee.getAvatar());
        }
        if(employee.getEmail() !=null){
            employeeBeforeUpdate.setEmail(employee.getEmail());
        }
        if(employee.getFirstName()!=null){
            employeeBeforeUpdate.setUsername(employee.getUsername());
        }
        if(employee.getGender()!=null){
            employeeBeforeUpdate.setGender(employee.getGender());
        }
        if(employee.getLastName()!=null){
            employeeBeforeUpdate.setLastName(employee.getLastName());
        }
        if(employee.getPassword() !=null){
            employeeBeforeUpdate.setPassword(employee.getPassword());
        }
        if(employee.getPhone()!=null){
            employeeBeforeUpdate.setPhone(employee.getPhone());
        }
        if(employee.getUnit()!=null){
            employeeBeforeUpdate.setUnit(employee.getUnit());
        }
        if(employee.getRoles().size() != employeeBeforeUpdate.getRoles().size()){
            employeeBeforeUpdate.setRoles(employee.getRoles());
            saveWithRoles(employeeBeforeUpdate);
        }else{
            save(employeeBeforeUpdate);
        }
        return  employeeBeforeUpdate;
    }

    public Page<Employee> findByRole(String roleTitle) {
        Role role = roleService.findByTitle(roleTitle);
        if(role == null){
            throw new EntityNotFoundException("Role does not exist!");
        }
        return new PageImpl<Employee>(role.getEmployees());
    }

    public Employee findByUsername(String username) {
        Employee employee = employeeRepository.findByUsername(username);
        if(employee == null){
            throw new EntityNotFoundException("Username does not exist!");
        }
        return employee;
    }
}
