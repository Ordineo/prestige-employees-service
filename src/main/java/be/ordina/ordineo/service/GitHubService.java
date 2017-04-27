package be.ordina.ordineo.service;

import be.ordina.ordineo.importusers.ScheduledTasks;
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
public class GitHubService {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private EmployeeRepository employeeRepository;

    private String ghOrganizationName;
    private String ghToken;

    private GitHub gh;
    private GHOrganization organization;

    public GitHubService(EmployeeRepository employeeRepository, String ghOrganizationName, String ghToken) throws IOException {
        this.employeeRepository = employeeRepository;
        this.ghOrganizationName = ghOrganizationName;
        this.ghToken = ghToken;
    }

    public void importUsers() throws IOException {
        gh = GitHub.connectUsingOAuth(ghToken);
        organization = gh.getOrganization(ghOrganizationName);

        organization.listMembers().asList().stream()
                .filter(member -> employeeRepository.findByGithubId(member.getId()) == null)
                .forEach(this::store);

        organization.listMembers().asList().stream()
                .filter(member -> employeeRepository.findByGithubId(member.getId()) != null)
                .forEach(this::update);
    }

    private void store(GHUser member) {
        Employee employee = new Employee(member.getLogin());
        LOG.info("Importing " + member.getLogin());
        try {
            employee.setGithubId(member.getId());
            employee.setEmail(member.getEmail());
            employee.setAvatar(member.getAvatarUrl());

            // Split first name and last name and put them in the according fields.
            if (member.getName() != null) {
                String[] splittedName = parseNames(member.getName().split("\\s+"));
                employee.setFirstName(splittedName[0]);
                employee.setLastName(splittedName[1]);
            }

            employee.setUnit(member.getCompany());
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
            String ghUserHandle = member.getLogin();
            String dbEmployeeHandle = dbEmployee.getUsername();

            // Compare string values with possible null value, if not the same: update
            if (!StringUtils.equals(ghUserHandle, dbEmployeeHandle)) {
                dbEmployee.setUsername(ghUserHandle);
                employeeRepository.save(dbEmployee);
                LOG.info(member.getLogin() + " handle updated.");
            }

            String ghUserEmail = member.getEmail();
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

    private String[] parseNames(String[] splittedName) {
        String firstname = "";
        String lastname = "";
        for (String namePart : splittedName) {
            if (namePart == splittedName[0]) firstname = namePart;
            else lastname += namePart + " ";
        }

        // Remove last space from lastname if there is one
        if (!lastname.equals(""))
            lastname = lastname.substring(0, lastname.length()-1);
        return new String[] {firstname, lastname};
    }

}
