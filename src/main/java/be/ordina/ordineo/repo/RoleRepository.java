package be.ordina.ordineo.repo;

import be.ordina.ordineo.model.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by SaFu on 14/04/2017.
 */
public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByTitle(@Param("title") String title);
}
