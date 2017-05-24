package be.ordina.ordineo.repository;

import be.ordina.ordineo.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.UUID;

/**
 * Created by shbe on 13/04/2017.
 */
@RepositoryRestResource
public interface EmployeeRepository extends JpaRepository<Employee, UUID> ,JpaSpecificationExecutor<Employee>{

  Employee findByUsername(@Param("username") String username);
  Employee findByGithubId(@Param("githubid") int githubId);

}
