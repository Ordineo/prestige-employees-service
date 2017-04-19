package be.ordina.ordineo.github;

import be.ordina.ordineo.model.Role;
import be.ordina.ordineo.repo.EmployeeRepository;
import be.ordina.ordineo.model.Employee;
import be.ordina.ordineo.repo.RoleRepository;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by SaFu on 14/04/2017.
 */
@Component
public class GithubController implements CommandLineRunner {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    // TODO Sheduled sync

    @Override
    public void run(String... args) throws Exception {

        // Admin config
        Role adminrole = new Role("admin");
        roleRepository.save(adminrole);
        Collection<Role> roles = new ArrayList<>();
        roles.add(adminrole);
        Employee admin = new Employee("admin", bCryptPasswordEncoder.encode("admin"), "sammi.fux@ordina.be",
                "Sammi", "Fux", "https://media.licdn.com/media/AAEAAQAAAAAAAAVgAAAAJDRjNGE2ZWVkLTM3NzEtNDNhZC1hMDdiLTI3NmU4MGU5Mzk0Zg.jpg",
                "0469696969", "JWORKS", "M", roles);

        employeeRepository.save(admin);

        GitHub gh = GitHub.connect();
        GHOrganization organization = gh.getOrganization("Ordineo");

        organization.listMembers().asList().forEach(member -> {
            employeeRepository.save(new Employee(member.getLogin()));
            GHUser ghUser = null;
            try {
                ghUser = gh.getUser(member.getLogin());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        System.out.println(organization.listMembers().asList());

    }
}
