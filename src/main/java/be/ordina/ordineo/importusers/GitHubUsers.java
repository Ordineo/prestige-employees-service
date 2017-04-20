package be.ordina.ordineo.importusers;

import be.ordina.ordineo.model.Employee;
import be.ordina.ordineo.repo.EmployeeRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
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

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private EmployeeRepository employeeRepository;

    GitHub gh;
    GHOrganization organization;

    public GitHubUsers(EmployeeRepository employeeRepository) throws IOException {
        this.employeeRepository = employeeRepository;
        gh = GitHub.connect();
        organization = gh.getOrganization("Ordineo");
    }

    public void importUsers() throws IOException {

        // Get GitHub user information
        organization.listMembers().forEach(member -> {
            String memberName = member.getLogin();

            // If the user is not found in database, it creates that user, else the user is skipped
            Employee dbEmployee = employeeRepository.findByUsername(memberName);
            if (dbEmployee == null) {
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
            } else {
                // If the GH user changed his email since the last time, the database gets updated.
                try {
                    String ghUserEmail = gh.getUser(memberName).getEmail();
                    String dbEmployeeEmail = dbEmployee.getEmail();

                    if (!(dbEmployeeEmail == null && ghUserEmail == null)) {
                        if (dbEmployeeEmail != null && ghUserEmail != null) {
                            if (!dbEmployeeEmail.equals(ghUserEmail)) {
                                dbEmployee.setEmail(ghUserEmail);
                                employeeRepository.save(dbEmployee);
                                log.info(memberName + " email updated.");
                            }
                        } else if ((dbEmployeeEmail != null && ghUserEmail == null) || (dbEmployeeEmail == null && ghUserEmail != null)) {
                            dbEmployee.setEmail(ghUserEmail);
                            employeeRepository.save(dbEmployee);
                            log.info(memberName + " email updated.");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
