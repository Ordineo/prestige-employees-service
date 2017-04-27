package be.ordina.ordineo.config;

import be.ordina.ordineo.model.Employee;
import be.ordina.ordineo.model.Role;
import be.ordina.ordineo.repository.EmployeeRepository;
import be.ordina.ordineo.repository.RoleRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

/**
 * Created by shbe on 26/04/2017.
 */
@Configuration
public class RepositoryConfig extends RepositoryRestConfigurerAdapter{
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
       //config.withEntityLookup().forValueRepository(EmployeeRepository.class, Employee::getUsername, EmployeeRepository::findByUsername);
      // config.withEntityLookup().forValueRepository(RoleRepository.class, Role::getTitle, RoleRepository::findByTitle);

    }
}
