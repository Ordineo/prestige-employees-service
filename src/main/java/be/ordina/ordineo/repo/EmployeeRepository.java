package be.ordina.ordineo.repo;

import be.ordina.ordineo.model.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Created by SaFu on 14/04/2017.
 */
public interface EmployeeRepository extends CrudRepository<Employee, UUID> {
    Employee findByUsername(@Param("username") String username);
    Employee findByGithubId(@Param("githubid") int githubId);
}
