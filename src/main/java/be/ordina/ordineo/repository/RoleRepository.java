package be.ordina.ordineo.repository;

import be.ordina.ordineo.model.Role;
//import be.ordina.ordineo.model.projection.RoleView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Created by shbe on 21/04/2017.
 */

@RepositoryRestResource//(excerptProjection = RoleView.class)
public interface RoleRepository  extends JpaRepository<Role,Long>{
    Role findByTitle(@Param("title") String title);
}
