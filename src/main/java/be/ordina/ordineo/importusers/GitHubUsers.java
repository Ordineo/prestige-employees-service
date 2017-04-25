package be.ordina.ordineo.importusers;

import be.ordina.ordineo.model.Employee;
import be.ordina.ordineo.repo.EmployeeRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by SaFu on 19/04/2017.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GitHubUsers {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private EmployeeRepository employeeRepository;

    private GitHub gh;
    private GHOrganization organization;

    public GitHubUsers(EmployeeRepository employeeRepository) throws IOException {
        this.employeeRepository = employeeRepository;
        gh = GitHub.connect();
        organization = gh.getOrganization("Ordineo");
    }

    public void importUsers() throws IOException {
        organization.listMembers().asList().stream()
                .filter(member -> employeeRepository.findByUsername(member.getLogin()) == null)
                .forEach(this::store);

        organization.listMembers().asList().stream()
                .filter(member -> employeeRepository.findByUsername(member.getLogin()) != null)
                .forEach(this::update);
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

    private void store(GHUser member) {
        Employee employee = new Employee(member.getLogin());
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
        LOG.info(member.getLogin() + " imported at: {}", dateFormat.format(new Date()));
    }

    private void update(GHUser member) {
        Employee dbEmployee = employeeRepository.findByUsername(member.getLogin());
        // If the GH user changed his email since the last time, the database gets updated.
        try {
            String ghUserEmail = gh.getUser(member.getLogin()).getEmail();
            String dbEmployeeEmail = dbEmployee.getEmail();

            // Compare string values with possible null value, if not the same: update
            if (!StringUtils.equals(ghUserEmail, dbEmployeeEmail)) {
                dbEmployee.setEmail(ghUserEmail);
                employeeRepository.save(dbEmployee);
                LOG.info(member.getLogin() + " email updated.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
