package be.ordina.ordineo.controller;

import be.ordina.ordineo.model.Employee;
import be.ordina.ordineo.model.Role;
import be.ordina.ordineo.model.projection.EmployeeView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by shbe on 23/05/2017.
 */
@Component
public class EmployeeViewResourceAssembler implements ResourceAssembler<EmployeeView,Resource<EmployeeView>> {

    /*@Autowired
    private RoleResourceAssembler roleResourceAssembler;
    @Autowired
    private EntityLinks entityLinks;

    public EmployeeViewResourceAssembler() {
    }*/
    private RoleResourceAssembler roleResourceAssembler;
    private EntityLinks entityLinks;

    public EmployeeViewResourceAssembler(RoleResourceAssembler roleResourceAssembler, EntityLinks entityLinks) {
        this.roleResourceAssembler = roleResourceAssembler;
        this.entityLinks = entityLinks;
    }

    public Link linkToSingleResource(EmployeeView employee) {
        Resource<EmployeeView> employeeResource = new Resource<EmployeeView>(employee);
        Link link = entityLinks.linkToSingleResource(Employee.class,employee.getUsername()).withSelfRel();
       //  Link link = linkTo(methodOn(EmployeeController.class).findByUsername(employee.getUsername())).withSelfRel();
        return link;
    }
    @Override
    public Resource<EmployeeView> toResource(EmployeeView employee) {
        Resource<EmployeeView> employeeResource = new Resource<>(employee);

        employeeResource.add(linkToSingleResource(employee));
        for(Role role:employee.getRoles()){
            employeeResource.add(roleResourceAssembler.linkToSingleResource(role).withRel("roles"));
        }
        return employeeResource;
    }
}
