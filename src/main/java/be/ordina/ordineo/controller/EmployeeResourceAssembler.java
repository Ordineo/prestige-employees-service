package be.ordina.ordineo.controller;

import be.ordina.ordineo.model.Employee;
import be.ordina.ordineo.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by shbe on 20/04/2017.
 */
@Component
public class EmployeeResourceAssembler implements ResourceAssembler<Employee,Resource<Employee>>{

    @Autowired
    private RoleResourceAssembler roleResourceAssembler;
    @Autowired
    private EntityLinks entityLinks;

    EmployeeResourceAssembler(){

    }

    public Link linkToSingleResource(Employee employee) {
        Resource<Employee> employeeResource = new Resource<Employee>(employee);
        Link link = entityLinks.linkToSingleResource(Employee.class,employee.getUsername());// username or id?????
       // Link link = linkTo(methodOn(EmployeeController.class).findByUsername(employee.getUsername())).withSelfRel();
        return link;
    }

    @Override
    public Resource<Employee> toResource(Employee employee) {
        Resource<Employee> employeeResource = new Resource<Employee>(employee);

        employeeResource.add(linkToSingleResource(employee));
       /* for(Role role:employee.getRoles()){
            employeeResource.add(roleResourceAssembler.linkToSingleResource(role).withRel("roles"));
        }*/
        return employeeResource;
    }

}
