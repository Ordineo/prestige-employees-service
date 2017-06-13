package be.ordina.ordineo.controller;

import be.ordina.ordineo.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

@Component
public class EmployeeResourceAssembler implements ResourceAssembler<Employee, Resource<Employee>> {

    private final EntityLinks entityLinks;

    @Autowired
    public EmployeeResourceAssembler(EntityLinks entityLinks) {
        this.entityLinks = entityLinks;
    }

    @Override
    public Resource<Employee> toResource(Employee entity) {
        final Resource<Employee> resource = new Resource<>(entity);

        resource.add(entityLinks.linkToSingleResource(Employee.class, entity.getUsername()).withSelfRel());
        resource.add(entityLinks.linkToSingleResource(Employee.class, entity.getUsername()).withRel("employee"));
        return resource;
    }
}
