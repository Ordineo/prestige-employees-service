package be.ordina.ordineo.importusers;

import be.ordina.ordineo.model.Role;
import be.ordina.ordineo.repo.EmployeeRepository;
import be.ordina.ordineo.model.Employee;
import be.ordina.ordineo.repo.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

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
        GitHubUsers gitHubUsers = new GitHubUsers(employeeRepository);
        gitHubUsers.importUsers();
    }

    private void createAdmin() {
        // Admin config
        Role adminrole = new Role("admin");
        roleRepository.save(adminrole);
        Collection<Role> roles = new ArrayList<>();
        roles.add(adminrole);
        Employee admin = new Employee("admin", bCryptPasswordEncoder.encode("default"), "sammi.fux@ordina.be",
                "Sammi", "Fux", "https://media.licdn.com/media/AAEAAQAAAAAAAAVgAAAAJDRjNGE2ZWVkLTM3NzEtNDNhZC1hMDdiLTI3NmU4MGU5Mzk0Zg.jpg",
                "0469696969", "JWORKS", "M", roles);

        employeeRepository.save(admin);
    }
}
