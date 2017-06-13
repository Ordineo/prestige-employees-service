package be.ordina.ordineo.controller;

import be.ordina.ordineo.TestConfig;
import be.ordina.ordineo.model.Role;
import be.ordina.ordineo.model.Role;
import be.ordina.ordineo.repository.RoleRepository;
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
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Maarten Casteels
 * @since 2017
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Profile("test")
public class RoleControllerTest {
    private MockMvc mockMvc;

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private RoleRepository roleRepository;

    @Before
    public void setUp() throws Exception {
        RestDocumentationResultHandler documentationHandler = document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()));
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(documentationHandler)
                .build();
    }

    @After
    public void tearDown() throws Exception {
        roleRepository.delete(roleRepository.findAll());
    }

    public void endpointIsExposed() throws URISyntaxException {
        Traverson traverson = new Traverson(new URI("http://localhost:" + getPort()), MediaTypes.HAL_JSON);
        ResponseEntity<ResourceSupport> index = traverson.follow().toEntity(ResourceSupport.class);
        assertThat(index, not(nullValue()));
    }

    @Test
    public void findAllRoles() throws Exception {
        createSampleRole("admin");
        createSampleRole("dev");

        mockMvc.perform(get("/roles").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.roles", hasSize(2)))
                .andExpect(jsonPath("$._embedded.roles[0].title", is("admin")))
                .andExpect(jsonPath("$._embedded.roles[1].title", is("dev")))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$.page").exists());
    }

    @Test
    public void findAllRolesWithPaging() throws Exception {
        for (int i = 0; i < 5; ++i) {
            createSampleRole(String.format("test%02d", i));
        }

        mockMvc.perform(get("/roles?page=1&size=2").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.roles", hasSize(2)))
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
    public void deleteRole() throws Exception {
        final Role admin = createSampleRole("admin"); // Insert already done

        Role byTitle = roleRepository.findByTitle("admin");
        assertThat(byTitle, equalTo(admin));

        mockMvc.perform(delete("/roles/admin")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent());

        Role role = roleRepository.findByTitle("roles");
        assertThat(role, is(nullValue()));
    }

    // Utils
    private Role createSampleRole(String roleName) {
        return roleRepository.save(new Role(roleName));
    }

    private int getPort() {
        return (((AnnotationConfigEmbeddedWebApplicationContext) webApplicationContext).getEmbeddedServletContainer()).getPort();
    }
}