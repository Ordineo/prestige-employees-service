package be.ordina.ordineo.controller;

import be.ordina.ordineo.TestConfig;
import be.ordina.ordineo.model.Employee;
import be.ordina.ordineo.repository.EmployeeRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.halLinks;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    private static final String USER_ACCOUNTS_NAME_SPACE = "as";
    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Autowired
    private WebApplicationContext webApplicationContext;

    private RestDocumentationResultHandler documentationHandler;

    @Autowired
    private EmployeeRepository personRepository;

    @Before
    public void setUp() throws Exception {
        documentationHandler = document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()));
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation)).alwaysDo(this.documentationHandler)
                .build();
    }

    @Test
    public void endpointIsExposed() throws URISyntaxException {
        Traverson traverson = new Traverson(new URI("http://localhost:" + getPort()), MediaTypes.HAL_JSON);
        ResponseEntity<ResourceSupport> index = traverson.follow().toEntity(ResourceSupport.class);
        assertThat(index).isNotNull();
    }

    @Test
    public void checkEmployees() throws Exception {
        createSamplePerson("George");
        createSamplePerson("Mary");

        mockMvc.perform(get("/employees").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    System.out.println(result);
                });
    }

//    @Test
//    public void listPeople() throws Exception {

    //
//        given().get("/").then().statusCode(HttpStatus.SC_OK);
//    }
//
    private Employee createSamplePerson(String username) {
        return personRepository.save(new Employee(username));
    }

    private int getPort() {
        return (((AnnotationConfigEmbeddedWebApplicationContext) webApplicationContext).getEmbeddedServletContainer()).getPort();
    }
}