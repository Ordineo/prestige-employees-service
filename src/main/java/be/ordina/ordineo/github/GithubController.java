package be.ordina.ordineo.github;

import be.ordina.ordineo.EmployeeRepository;
import be.ordina.ordineo.model.Employee;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by SaFu on 14/04/2017.
 */
@Component
public class GithubController implements CommandLineRunner {

    @Autowired
    EmployeeRepository employeeRepository;

    // TODO Sheduled sync

    @Override
    public void run(String... args) throws Exception {
        GitHub gh = GitHub.connect();
        GHOrganization organization = gh.getOrganization("Ordineo");
        System.out.println(organization.listMembers().asList());

        organization.listMembers().asList().forEach(member -> employeeRepository.save(new Employee(member.getLogin())));

    }
}
