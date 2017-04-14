package be.ordina.ordineo;

import be.ordina.ordineo.model.Employee;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

/**
 * Created by SaFu on 14/04/2017.
 */
public interface EmployeeRepository extends CrudRepository<Employee, UUID> {

}
