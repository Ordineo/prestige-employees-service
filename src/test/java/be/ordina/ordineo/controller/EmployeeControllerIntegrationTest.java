package be.ordina.ordineo.controller;

import be.ordina.ordineo.model.Employee;
import be.ordina.ordineo.model.Gender;
import be.ordina.ordineo.model.Unit;
import be.ordina.ordineo.repository.EmployeeRepository;
import be.ordina.ordineo.service.EmployeeService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by shbe on 08/05/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SqlGroup({

@Sql(executionPhase = BEFORE_TEST_METHOD, scripts = {"classpath:schema.sql", "classpath:import.sql"
}),
@Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:afterTestRun.sql")
})
public class EmployeeControllerIntegrationTest {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private EmployeeRepository employeeRepository;
    private RestDocumentationResultHandler documentHandler;
    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Before
    public void setUp() {
        this.documentHandler = document("{method-name}",
                preprocessRequest(prettyPrint()),//removeHeaders("Foo")????
                preprocessResponse(prettyPrint()));
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(documentationConfiguration(this.restDocumentation))
                .alwaysDo(this.documentHandler)
                .build();
    }

  /*  @Test
    public void findByUsername() throws Exception {

        this.mockMvc.perform(get("/employees/{username}", "JLEFE1")
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.unit",is("JWORKS")))
                .andExpect(jsonPath("$.username",is("JLEFE1")))
                .andExpect(jsonPath("$.lastName",is("Lefever")))
                .andExpect(jsonPath("$.firstName",is("Joris")))
                .andExpect(jsonPath("$.avatar" ,
                        is( "http://d33v4339jhl8k0.cloudfront.net/docs/assets/528e78fee4b0865bc066be5a/images/52af1e8ce4b074ab9e98f0e0/file-mxuiNezRS5.jpg")))
                .andExpect(jsonPath("$.gender",is("MALE")))
                .andExpect(jsonPath("$.phone",is("0469696969")))
                //.andExpect(jsonPath("$._embedded.role.title",is("senior"))) how to check this?????
                .andExpect(jsonPath("$.email" , is("joris.lefever@ordina.be")))
        .andExpect(jsonPath())"$._links.self.href", endsWith
                .andDo(this.documentHandler.document());
    }*/

    @Test
    public void findAll() throws Exception {
        this.mockMvc.perform(
                get("/employees/")
                        .param("page", "0")
                        .param("size", "3")
                        .param("sort", "firstName,desc")
                        .param("search", "lastName:Last")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalElements", is(5)))
                .andExpect(jsonPath("$.page.totalPages", is(2)))
                .andExpect(jsonPath("$.page.number", is(0)))
                .andExpect(jsonPath("$.page.size", is(3)));

    }
}
