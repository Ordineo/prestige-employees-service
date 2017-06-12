package be.ordina.ordineo.importusers;

import be.ordina.ordineo.service.GitHubService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class ScheduledTasks {

    private final GitHubService gitHubService;

    @Autowired
    public ScheduledTasks(final GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void importGitHubUsers() {
      log.info("Import users from GitHub service");
        try {
            gitHubService.importUsers();
        } catch (final IOException ioE) {
            log.error("Error occurred when importing", ioE);
        }
    }

}
