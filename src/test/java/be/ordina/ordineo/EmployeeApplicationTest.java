package be.ordina.ordineo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Maarten Casteels
 * @since 2017
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@Profile("test")
public class EmployeeApplicationTest {

    @Test
    public void loadContext() throws Exception {
    }
}