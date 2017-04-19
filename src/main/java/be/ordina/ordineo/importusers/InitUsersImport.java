package be.ordina.ordineo.importusers;

import be.ordina.ordineo.model.Role;
import be.ordina.ordineo.repo.EmployeeRepository;
import be.ordina.ordineo.model.Employee;
import be.ordina.ordineo.repo.RoleRepository;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Created by SaFu on 14/04/2017.
 */
@Component
public class InitUsersImport implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void run(String... args) throws Exception {
        log.info("Initial users import started...");
        createAdmin();
        initImportUsers();
    }

    private void createAdmin() {
        // Admin config
        Role adminrole = new Role("admin");
        roleRepository.save(adminrole);
        Collection<Role> roles = new ArrayList<>();
        roles.add(adminrole);
        Employee admin = new Employee("admin", bCryptPasswordEncoder.encode("admin"), "sammi.fux@ordina.be",
                "Sammi", "Fux", "https://media.licdn.com/media/AAEAAQAAAAAAAAVgAAAAJDRjNGE2ZWVkLTM3NzEtNDNhZC1hMDdiLTI3NmU4MGU5Mzk0Zg.jpg",
                "0469696969", "JWORKS", "M", roles);

        employeeRepository.save(admin);
    }

    private void initImportUsers() throws IOException {
        GitHub gh = GitHub.connect();
        GHOrganization organization = gh.getOrganization("Ordineo");

        // Get GitHub user information
        ArrayList<GHUser> ghUsers = (ArrayList<GHUser>) organization.listMembers().asList();
        ghUsers.forEach(member -> {
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
                log.info(memberName + " already in database.");
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
