package be.ordina.ordineo.controller;

import be.ordina.ordineo.model.Employee;
import be.ordina.ordineo.model.Gender;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

/**
 * @author Maarten Casteels
 * @since 2017
 */
@RunWith(MockitoJUnitRunner.class)
public class EmployeeResourceAssemblerTest {

    @Mock
    private EntityLinks entityLinks;

    @InjectMocks
    private EmployeeResourceAssembler assembler;

    @Before
    public void setUp() throws Exception {
        // Nothing yet
    }

    @After
    public void tearDown() throws Exception {
        Mockito.verifyNoMoreInteractions(entityLinks);
    }

    @Test
    public void shouldAssembleEmployeeWhenEmployeeIsGiven() throws Exception {
        final Employee entity = new Employee();
        entity.setUsername("johndoe");
        entity.setFirstName("John");
        entity.setLastName("Doe");
        entity.setEnabled(1);
        entity.setGender(Gender.MALE);

        final Link self = new Link("http://localhost:8080/employee/johndoe");
        final Link employee = new Link("http://localhost:8080/employee/johndoe", "employee");
        Mockito.when(entityLinks.linkToSingleResource(Employee.class, entity.getUsername())).thenReturn(self, employee);

        final Resource<Employee> resource = assembler.toResource(entity);
        assertThat(resource.getContent(), equalTo(entity));
        assertThat(resource.getLinks(), hasSize(2));
        assertThat(resource.getLink("self"), equalTo(self));
        assertThat(resource.getLink("employee"), equalTo(employee));

        Mockito.verify(entityLinks, Mockito.times(2)).linkToSingleResource(Employee.class, entity.getUsername());
    }
}