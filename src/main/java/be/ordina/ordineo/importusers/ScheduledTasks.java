package be.ordina.ordineo.importusers;

import be.ordina.ordineo.repo.EmployeeRepository;
import be.ordina.ordineo.service.GitHubService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Created by SaFu on 19/04/2017.
 */
@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private EmployeeRepository employeeRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void importGitHubUsers() throws IOException {
        GitHubService gitHubService = new GitHubService(employeeRepository);
        gitHubService.importUsers();
    }

}
