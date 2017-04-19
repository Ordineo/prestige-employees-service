package be.ordina.ordineo.importusers;

import be.ordina.ordineo.model.Employee;
import be.ordina.ordineo.repo.EmployeeRepository;
import lombok.NoArgsConstructor;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by SaFu on 19/04/2017.
 */
@NoArgsConstructor
@Component
public class ImportGitHubUsers {

    public void importAndUpdate() throws IOException {

    }


}
