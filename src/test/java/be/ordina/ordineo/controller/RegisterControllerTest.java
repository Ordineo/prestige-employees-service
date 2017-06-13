package be.ordina.ordineo.controller;

import be.ordina.ordineo.TestConfig;
import be.ordina.ordineo.model.Employee;
import be.ordina.ordineo.model.Register;
import be.ordina.ordineo.repository.EmployeeRepository;
import be.ordina.ordineo.util.TestUtil;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;
import java.net.URISyntaxException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Maarten Casteels
 * @since 2017
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Profile("test")
public class RegisterControllerTest {
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
    public void shouldEnableEmployee() throws Exception {
        final Employee entity = new Employee();
        entity.setUsername("johndoe");
        entity.setEnabled(0);
        entity.setFirstName("John");
        entity.setLastName("Doe");
        employeeRepository.save(entity);

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(new Register("johndoe", "password")))
        ).andExpect(status().isNoContent());

        final Employee employee = employeeRepository.findByUsername("johndoe");
        assertThat(employee.getEnabled(), is(equalTo(1)));
        assertThat(employee.getPassword(), is(notNullValue()));
    }

    // Utils
    private int getPort() {
        return (((AnnotationConfigEmbeddedWebApplicationContext) webApplicationContext).getEmbeddedServletContainer()).getPort();
    }
}