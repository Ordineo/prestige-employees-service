package be.ordina.ordineo.service;

import be.ordina.ordineo.repository.EmployeeRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

/**
 * @author Maarten Casteels
 * @since 2017
 */
@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private EmployeeService service;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
        Mockito.verifyNoMoreInteractions(
                employeeRepository,
                roleService
        );
    }

    @Test
    public void name() throws Exception {
        assertTrue(true);
    }
}