package be.ordina.ordineo;

import be.ordina.ordineo.service.GitHubService;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author Maarten Casteels
 * @since 2017
 */
@Configuration
@SpringBootApplication
@Profile("test")
public class TestConfig {

    @Bean
    public GitHubService gitHubService() {
        return Mockito.mock(GitHubService.class);
    }

}
