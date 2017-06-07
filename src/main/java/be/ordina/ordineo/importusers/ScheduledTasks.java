package be.ordina.ordineo.importusers;

import be.ordina.ordineo.service.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ScheduledTasks {

    private final GitHubService gitHubService;

    @Autowired
    public ScheduledTasks(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void importGitHubUsers() throws IOException {
        gitHubService.importUsers();
    }

}
