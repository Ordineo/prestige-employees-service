package be.ordina.ordineo.controller;

import be.ordina.ordineo.TestConfig;
import be.ordina.ordineo.model.Employee;
import be.ordina.ordineo.model.Gender;
import be.ordina.ordineo.repository.EmployeeRepository;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Maarten Casteels
 * @since 2017
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Profile("test")
public class EmployeeControllerTest {
    private MockMvc mockMvc;

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private EmployeeRepository employeeRepository;

    private RestDocumentationResultHandler documentationHandler;

    @Before
    public void setUp() throws Exception {
        documentationHandler = document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()));
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(this.documentationHandler)
                .build();
    }

    @After
    public void tearDown() throws Exception {
        employeeRepository.delete(employeeRepository.findAll());
    }

    @Test
    public void endpointIsExposed() throws URISyntaxException {
        Traverson traverson = new Traverson(new URI("http://localhost:" + getPort()), MediaTypes.HAL_JSON);
        ResponseEntity<ResourceSupport> index = traverson.follow().toEntity(ResourceSupport.class);
        assertThat(index, not(nullValue()));
    }

    @Test
    public void findAllEmployees() throws Exception {
        createSamplePerson("George");
        createSamplePerson("Mary");

        mockMvc.perform(get("/employees").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.employees", hasSize(2)))
                .andExpect(jsonPath("$._embedded.employees[0].username", is("george")))
                .andExpect(jsonPath("$._embedded.employees[0].firstName", is("George")))
                .andExpect(jsonPath("$._embedded.employees[1].username", is("mary")))
                .andExpect(jsonPath("$._embedded.employees[1].firstName", is("Mary")))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$.page").exists());
    }

    @Test
    public void findAllEmployeesWithPaging() throws Exception {
        for (int i = 0; i < 5; ++i) {
            createSamplePerson(String.format("test%02d", i));
        }

        mockMvc.perform(get("/employees?page=1&size=2").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.employees", hasSize(2)))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$._links.first").exists())
                .andExpect(jsonPath("$._links.prev").exists())
                .andExpect(jsonPath("$._links.next").exists())
                .andExpect(jsonPath("$._links.last").exists())
                .andExpect(jsonPath("$.page").exists())
                .andExpect(jsonPath("$.page.totalElements", is(5)));
    }

    @Test
    public void findAllEmployeesWithSearchCriteria() throws Exception {
        createSamplePerson("George");
        createSamplePerson("Mary");

        mockMvc.perform(get("/employees?search=username:g").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.employees", hasSize(1)))
                .andExpect(jsonPath("$._embedded.employees[0].username", is("george")))
                .andExpect(jsonPath("$._embedded.employees[0].firstName", is("George")))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$.page").exists());
    }

    @Test
    public void findAllEmployeesWithSearchCriteriaAndPaging() throws Exception {
        for (int i = 0; i < 5; ++i) {
            createSamplePerson(String.format("George%02d", i));
        }
        for (int i = 0; i < 2; ++i) {
            createSamplePerson(String.format("Mary%02d", i));
        }

        mockMvc.perform(get("/employees?search=username:g&page=1&size=2").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.employees", hasSize(2)))
                .andExpect(jsonPath("$._embedded.employees[0].username", containsString("george")))
                .andExpect(jsonPath("$._embedded.employees[0].firstName", containsString("George")))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$._links.first").exists())
                .andExpect(jsonPath("$._links.next").exists())
                .andExpect(jsonPath("$._links.last").exists())
                .andExpect(jsonPath("$.page").exists())
                .andExpect(jsonPath("$.page.totalElements", is(5)));
    }

    @Test
    public void checkEmployeeByUsername() throws Exception {
        createSamplePerson("George");
        createSamplePerson("Mary");

        mockMvc.perform(get("/employees/george").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("george")))
                .andExpect(jsonPath("$.firstName", is("George")));
    }

    @Test
    public void updateEmployee() throws Exception {
        final Employee george = createSamplePerson("George"); // Insert already done
        george.setGender(Gender.MALE);
        george.setLastName("Demeyere");

        Employee byUsername = employeeRepository.findByUsername("george");
        assertThat(byUsername, not(equalTo(george))); // Validate that the update in the database not has been performed

        mockMvc.perform(put("/employees/george")
                .content(convertObjectToJsonBytes(george))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent());

        byUsername = employeeRepository.findByUsername("george");
        assertThat(byUsername, equalTo(george));
    }

    @Test
    public void deleteEmployee() throws Exception {
        final Employee george = createSamplePerson("George"); // Insert already done

        Employee byUsername = employeeRepository.findByUsername("george");
        assertThat(byUsername, equalTo(george));

        mockMvc.perform(delete("/employees/george")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent());

        byUsername = employeeRepository.findByUsername("george");
        assertThat(byUsername.getEnabled(), equalTo(0));
        assertThat(byUsername.getDeleted(), equalTo(1));
    }

    // Utils
    private Employee createSamplePerson(String firstName) {
        final Employee s = new Employee();
        s.setFirstName(firstName);
        s.setUsername(firstName.toLowerCase());
        s.setEnabled(1);
        return employeeRepository.save(s);
    }

    private int getPort() {
        return (((AnnotationConfigEmbeddedWebApplicationContext) webApplicationContext).getEmbeddedServletContainer()).getPort();
    }

    public byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }
}