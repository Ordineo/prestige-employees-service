package be.ordina.ordineo.service;

import be.ordina.ordineo.exception.EntityNotFoundException;
import be.ordina.ordineo.model.Employee;
import be.ordina.ordineo.model.Role;
import be.ordina.ordineo.repository.EmployeeRepository;
import be.ordina.ordineo.repository.RoleRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * @author Maarten Casteels
 * @since 2017
 */
@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService service;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
        Mockito.verifyNoMoreInteractions(
                employeeRepository
        );
    }

    @Test
    public void shouldSoftDeleteEmployeeWhenExecutingDelete() throws Exception {
        final ArgumentCaptor<Employee> captor = ArgumentCaptor.forClass(Employee.class);

        final Employee employee = new Employee();
        employee.setEnabled(1);
        employee.setUsername("johndoe");

        Mockito.when(employeeRepository.findByUsername("johndoe")).thenReturn(employee);
        Mockito.when(employeeRepository.save(captor.capture())).thenReturn(null);

        service.delete("johndoe");

        assertThat(captor.getValue().getDeleted(), is(equalTo(1)));

        Mockito.verify(employeeRepository).findByUsername("johndoe");
        Mockito.verify(employeeRepository).save(captor.getValue());
    }

    @Test
    public void shouldFailWhenEmployeeNotFound() throws Exception {
        try {
            service.findByUsername("johndoe");
            fail();
        } catch (final EntityNotFoundException enfEx) {
            assertThat(enfEx.getMessage(), is(equalTo("Username does not exists!")));
        }
        Mockito.verify(employeeRepository).findByUsername("johndoe");
    }

    @Test
    public void shouldReturnEmployeeWhenFound() throws Exception {
        final Employee employee = new Employee();
        employee.setEnabled(1);
        employee.setUsername("johndoe");

        Mockito.when(employeeRepository.findByUsername("johndoe")).thenReturn(employee);

        service.findByUsername("johndoe");

        Mockito.verify(employeeRepository).findByUsername("johndoe");
    }

    @Test
    public void shouldSaveEmployee() throws Exception {
        final ArgumentCaptor<Employee> captor = ArgumentCaptor.forClass(Employee.class);

        final Employee employee = new Employee();
        employee.setEnabled(1);
        employee.setUsername("johndoe");

        service.save(employee);

        Mockito.verify(employeeRepository).save(captor.capture());
        assertThat(captor.getValue(), is(equalTo(employee)));
    }

    @Test
    public void shouldUpdateEmployee() throws Exception {
        final ArgumentCaptor<Employee> captor = ArgumentCaptor.forClass(Employee.class);

        final Employee employee = new Employee();
        employee.setEnabled(1);
        employee.setUsername("johndoe");
        employee.setRoles(Collections.singletonList(new Role("admin")));

        final Employee updateEmployee = new Employee();
        updateEmployee.setFirstName("John");
        updateEmployee.setLastName("Doe");
        updateEmployee.setEnabled(1);
        updateEmployee.setUsername("johndoe");
        Mockito.when(employeeRepository.findByUsername("johndoe")).thenReturn(employee);
        Mockito.when(employeeRepository.save(captor.capture())).thenReturn(null);

        service.update("johndoe", updateEmployee);

        assertThat(captor.getValue().getRoles().get(0).getTitle(), is(equalTo("admin")));
        assertThat(captor.getValue().getFirstName(), is(equalTo("John")));
        assertThat(captor.getValue().getLastName(), is(equalTo("Doe")));
        assertThat(captor.getValue().getUuid(), is(equalTo(employee.getUuid())));

        Mockito.verify(employeeRepository).findByUsername("johndoe");
        Mockito.verify(employeeRepository).save(captor.capture());
    }
}