package be.ordina.ordineo.service;

import be.ordina.ordineo.exception.EntityNotFoundException;
import be.ordina.ordineo.model.Employee;
import be.ordina.ordineo.model.Gender;
import be.ordina.ordineo.model.Role;
import be.ordina.ordineo.model.Unit;
import be.ordina.ordineo.repository.EmployeeRepository;
import com.google.common.base.Verify;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;


import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by shbe on 09/05/2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private RoleService roleService;
    @InjectMocks
    private EmployeeService employeeService;
    private Employee employee;
    @Before
    public void setUp() {
        employeeService = new EmployeeService();
        MockitoAnnotations.initMocks(this);
    }

  /*  @Test
    public void testFindAllEmployeeSuccess() {
        List<Employee> employees = new ArrayList<>();
        Page<Employee> employeePage = new PageImpl<Employee>(employees);
        when(employeeRepository.findAll(any(Specification.class),any(Pageable.class))).thenReturn(employeePage);
        Page<Employee> employeePageExpected = employeeService.findAll(Optional.of("lastName:Lef"),null);
        ArgumentCaptor<Pageable> pageArgument = ArgumentCaptor.forClass(Pageable.class);
        verify(employeeRepository, times(1)).findAll(any(Specification.class), pageArgument.capture());

    }*/
    @Test
    public void testFindByUsernameIfUsernameNotFound(){
        when(employeeRepository.findByUsername("usernamewhichdoesnotexist")).thenReturn(null);
        try {
            employeeService.findByUsername("usernamewhichdoesnotexist");
        }catch (EntityNotFoundException ex){
            verify(employeeRepository,times(1)).findByUsername("usernamewhichdoesnotexist");
            assertEquals(ex.getMessage(),"Username does not exist!");
            verifyNoMoreInteractions(employeeRepository);
        }
    }

    @Test(expected = EntityNotFoundException.class)
    public void testFindByRoleIfRoleNotFound() {
        when(roleService.findByTitle("sometitlewhichdoesnotexist")).thenReturn(null);
        Page<Employee> employees = employeeService.findByRole("sometitlewhichdoesnotexist");
    }

    @Test
    public void testDeleteEmployeeWithNoRolesSuccess() throws EntityNotFoundException {
        employee = createEmployee();
        when(employeeRepository.findByUsername(employee.getUsername())).thenReturn(employee);
        employeeService.delete(employee.getUsername());
        verify(employeeRepository, times(1)).findByUsername(employee.getUsername());
        verify(employeeRepository, times(1)).delete(employee.getUuid());
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    public void testDeleteEmployeeIfEmployeeNotFound() {
        employee = createEmployee();
        when(employeeRepository.findByUsername(employee.getUsername())).thenReturn(null);
        try {
            employeeService.delete(employee.getUsername());
            fail();
        } catch (EntityNotFoundException ex) {
            verify(employeeRepository, times(1)).findByUsername(employee.getUsername());
            verifyNoMoreInteractions(employeeRepository);
        }

    }

/*      @Test
      public void testDeleteEmployeeWithRolesSuccess() {
          employee = createEmployee();
          List<Role> roles = new ArrayList<>();
          roles.add(createRole());
          employee.setRoles(roles);
          when(employeeService.findByUsername(employee.getUsername())).thenReturn(employee);
?????????

      }*/
    @Test
    public void testSaveEmployeeWithNoRolesSuccess() {
        employee = createEmployee();
        Employee employeeToCreate = createEmployee();
        employeeToCreate.setUuid(null);
        ArgumentCaptor<Employee> employeeArgument = ArgumentCaptor.forClass(Employee.class);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
       // when(employeeRepository.save(employeeArgument.capture())).thenReturn(employee);
        Employee employeeCreated = employeeService.save(employeeToCreate);
        verify(employeeRepository, times(1)).save(employeeArgument.capture());
        verifyNoMoreInteractions(employeeRepository);
        assertEquals(employee, employeeCreated);
    }
   /* @Test
    public void testSaveEmployeeWithRolesSuccess() {
        employee = createEmployee();
        List<Role> roles = new ArrayList<>();
        roles.add(createRole());
        doNothing().when()?????????
    }*/
    @Test(expected = EntityNotFoundException.class)
    public void testUpdateEmployeeIfEmployeeNotFound(){
        employee = createEmployee();
        employee.setUsername("usernamedoesnotexist");
        when(employeeRepository.findByUsername(employee.getUsername())).thenReturn(null);
        employeeService.update(employee.getUsername(),employee);
    }
  /*  @Test
    public void testUpdateEmployeeSuccess(){
        Employee existingEmployee = createEmployee();
        employee = createEmployee();
        employee.setUsername("idsh");
        when(employeeRepository.findByUsername(existingEmployee.getUsername())).thenReturn(existingEmployee);
        employeeService.update(existingEmployee.getUsername(),employee);
        verify(employeeRepository,times(1)).findByUsername(existingEmployee.getUsername());
       // verifyNoMoreInteractions(employeeRepository);
       // assertEquals(employee,updatedEmployee);

    }*/
    private Employee createEmployee() {
        Employee employee = new Employee();
        employee.setUsername("JLEFE1");
        employee.setFirstName("Joris");
        employee.setLastName("Lefever");
        employee.setUnit(Unit.JWORKS);
        employee.setPhone("0469696969");
        employee.setPassword("derp");
        employee.setGender(Gender.MALE);
        employee.setAvatar("http://d33v4339jhl8k0.cloudfront.net/docs/assets/528e78fee4b0865bc066be5a/images/52af1e8ce4b074ab9e98f0e0/file-mxuiNezRS5.jpg");
        employee.setUuid(UUID.randomUUID());
        employee.setEmail("joris.lefever@ordina.be");
        return employee;
    }

    private Role createRole() {
        Role role = new Role();
        role.setId(1L);
        role.setTitle("rookie");
        return role;
    }
}
