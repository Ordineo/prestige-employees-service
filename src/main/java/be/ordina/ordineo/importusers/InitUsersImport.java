package be.ordina.ordineo.importusers;

import be.ordina.ordineo.model.Employee;
import be.ordina.ordineo.model.Role;
import be.ordina.ordineo.repository.EmployeeRepository;
import be.ordina.ordineo.repository.RoleRepository;
import be.ordina.ordineo.service.GitHubService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class InitUsersImport implements ApplicationListener<ApplicationReadyEvent>{

    private final EmployeeRepository employeeRepository;

    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final GitHubService gitHubService;

    @Autowired
    public InitUsersImport(EmployeeRepository employeeRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder, GitHubService gitHubService) {
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.gitHubService = gitHubService;
    }

//    @Override
//    public void run(String... args) throws Exception {
//
//    }

    private void createAdmin() {
        // Admin config
        Role adminrole = new Role("admin");
        roleRepository.save(adminrole);
        List<Role> roles = new ArrayList<>();
        roles.add(adminrole);
        Employee admin = new Employee("admin", bCryptPasswordEncoder.encode("default"), "sammi.fux@ordina.be",
                "Sammi", "Fux", "https://media.licdn.com/media/AAEAAQAAAAAAAAVgAAAAJDRjNGE2ZWVkLTM3NzEtNDNhZC1hMDdiLTI3NmU4MGU5Mzk0Zg.jpg",
                "0469696969", "JWORKS", "M", roles);

        employeeRepository.save(admin);
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        log.info("Initial users import started...");
        createAdmin();
        try {
            gitHubService.importUsers();
        } catch (IOException e) {
            log.error("Could not import users with the github service", e);
        }
    }
}
