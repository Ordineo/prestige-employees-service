package be.ordina.ordineo.repository;

import be.ordina.ordineo.model.Employee;
//import be.ordina.ordineo.model.projection.EmployeeView;
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
@RepositoryRestResource//(excerptProjection = EmployeeView.class)
public interface EmployeeRepository extends JpaRepository<Employee, UUID> ,JpaSpecificationExecutor<Employee>{

  //@RestResource(path="employee",rel="employee")
  Employee findByUuid(@Param("uuid") UUID uuid);
  Employee findByUsername(@Param("username") String username);
  //Page<Employee> findAll(@Param("projection") String projection, @Param("pageRequest") Pageable pageRequest);
  //Page<Employee> findAll(Specification<Employee> specification,  Pageable pageRequest);


}
