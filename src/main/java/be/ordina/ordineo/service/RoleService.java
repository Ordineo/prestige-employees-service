package be.ordina.ordineo.service;

import be.ordina.ordineo.exception.EntityNotFoundException;
import be.ordina.ordineo.model.Employee;
import be.ordina.ordineo.model.Role;
import be.ordina.ordineo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by shbe on 21/04/2017.
 */
@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private EmployeeService employeeService;

   /* @Autowired
    public RoleService(RoleRepository roleRepository,EmployeeService employeeService) {
        this.roleRepository = roleRepository;
        this.employeeService = employeeService;
    }*/


    public Role save(Role role) {
        Role savedRole = roleRepository.save(role);
        return savedRole;
    }

    public List<Role> saveAll(List<Role> roles) {
        List<Role> savedRoles = roleRepository.save(roles);
        return savedRoles;
    }

    public Role findByTitle(String title) {
        Role role = roleRepository.findByTitle(title);
        if (role == null) {
            throw new EntityNotFoundException("Role does not exist!");
        }
        return role;
    }

    public Page<Role> findAll() {
        return new PageImpl<Role>(roleRepository.findAll());
    }

    @Transactional//definately ask this transactionl shouldn't be from spring frame work???? instead of javax
    public void delete(String title) {
        Role role = findByTitle(title);

//        if (!role.getEmployees().isEmpty()) {
//            unlinkEmployee(role);
//        }
        roleRepository.delete(role.getId());
    }

    @Transactional
    private void unlinkEmployee(Role role) {
        List<Employee> employees = role.getEmployees();
        for (Employee employee : employees) {
            if (employee.getRoles().contains(role)) {
                employee.getRoles().remove(role);
            }
        }
        role.getEmployees().removeAll(employees);
//        employeeService.saveAll(employees);
    }

}
