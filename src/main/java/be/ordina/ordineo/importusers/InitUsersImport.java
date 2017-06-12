package be.ordina.ordineo.importusers;

import be.ordina.ordineo.service.GitHubService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class InitUsersImport implements ApplicationListener<ApplicationReadyEvent> {

    private final GitHubService gitHubService;

    @Autowired
    public InitUsersImport(final GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        log.info("Initial users import started...");
        try {
            gitHubService.importUsers();
        } catch (final IOException ioE) {
            log.error("Could not import users with the GitHub service", ioE);
        }
    }
}
