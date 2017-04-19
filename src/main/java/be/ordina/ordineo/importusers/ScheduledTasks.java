package be.ordina.ordineo.importusers;

import be.ordina.ordineo.model.Employee;
import be.ordina.ordineo.repo.EmployeeRepository;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by SaFu on 19/04/2017.
 */
@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    EmployeeRepository employeeRepository;

    private GitHub gh;
    private GHOrganization organization;

    @Scheduled(cron = "0 0 0 * *")
    public void importGitHubUsers() throws IOException {

        gh = GitHub.connect();
        organization = gh.getOrganization("Ordineo");

        // Get GitHub user information
        organization.listMembers().asList().forEach(member -> {
            String memberName = member.getLogin();

            // If the user is not found in database, it creates that user, else the user is skipped
            if (employeeRepository.findByUsername(memberName) == null) {
                Employee employee = new Employee(memberName);
                GHUser ghUser;
                try {
                    ghUser = gh.getUser(member.getLogin());
                    employee.setEmail(ghUser.getEmail());
                    employee.setAvatar(ghUser.getAvatarUrl());

                    // Split first name and last name and put them in the according fields.
                    if (ghUser.getName() != null) {
                        String[] splittedName = parseNames(ghUser.getName().split("\\s+"));
                        employee.setFirstName(splittedName[0]);
                        employee.setLastName(splittedName[1]);
                    }

                    employee.setUnit(ghUser.getCompany());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                employeeRepository.save(employee);
                log.info(memberName + " imported at: {}", dateFormat.format(new Date()));
            } else
                System.out.println(memberName + " already in database.");
        });
    }

    private String[] parseNames(String[] splittedName) {
        String firstname = "";
        String lastname = "";
        for (String namePart : splittedName) {
            if (namePart == splittedName[0]) firstname = namePart;
            else lastname += namePart + " ";
        }
        return new String[] {firstname, lastname.substring(0, lastname.length()-1)};
    }

}
