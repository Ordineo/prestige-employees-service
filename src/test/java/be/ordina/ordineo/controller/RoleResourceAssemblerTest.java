package be.ordina.ordineo.controller;

import be.ordina.ordineo.model.Employee;
import be.ordina.ordineo.model.Gender;
import be.ordina.ordineo.model.Role;
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
public class RoleResourceAssemblerTest {
    @Mock
    private EntityLinks entityLinks;

    @InjectMocks
    private RoleResourceAssembler assembler;

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
        final Role entity = new Role();
        entity.setTitle("admin");

        final Link self = new Link("http://localhost:8080/roles/admin");
        final Link employee = new Link("http://localhost:8080/roles/admin", "role");
        Mockito.when(entityLinks.linkToSingleResource(Role.class, entity.getTitle())).thenReturn(self, employee);

        final Resource<Role> resource = assembler.toResource(entity);
        assertThat(resource.getContent(), equalTo(entity));
        assertThat(resource.getLinks(), hasSize(2));
        assertThat(resource.getLink("self"), equalTo(self));
        assertThat(resource.getLink("role"), equalTo(employee));

        Mockito.verify(entityLinks, Mockito.times(2)).linkToSingleResource(Role.class, entity.getTitle());
    }
}