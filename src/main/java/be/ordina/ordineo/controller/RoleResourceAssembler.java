package be.ordina.ordineo.controller;

import be.ordina.ordineo.model.Employee;
import be.ordina.ordineo.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by shbe on 26/04/2017.
 */
@Component
public class RoleResourceAssembler implements ResourceAssembler<Role, Resource<Role>> {
    private EmployeeResourceAssembler employeeResourceAssembler;
    private EntityLinks entityLinks;

    @Autowired
    public RoleResourceAssembler(EmployeeResourceAssembler employeeResourceAssembler, EntityLinks entityLinks) {
        this.employeeResourceAssembler = employeeResourceAssembler;
        this.entityLinks = entityLinks;
    }


    /*  public RoleResourceAssembler() {

     }*/
    public Link linkToSingleResource(Role role) {
        //  return linkTo(RoleController.class).slash(role).withSelfRel();
        return entityLinks.linkToSingleResource(Role.class, role.getTitle());//title or id????
    }

    @Override
    public Resource<Role> toResource(Role role) {
        Resource<Role> roleResource = new Resource<>(role);
        roleResource.add(linkToSingleResource(role));
        // roleResource.add((linkTo(RoleController.class).slash(role)).withSelfRel());
        //  Link employeesLink = linkTo(methodOn(EmployeeController.class).findByRole(role.getTitle())).withRel("employees");
        for (Employee employee : role.getEmployees()) {
            roleResource.add(employeeResourceAssembler.linkToSingleResource(employee).withRel("employees"));
        }


        return roleResource;
    }
}
