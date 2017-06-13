package be.ordina.ordineo.controller;

import be.ordina.ordineo.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

@Component
public class RoleResourceAssembler implements ResourceAssembler<Role, Resource<Role>> {
    private EntityLinks entityLinks;

    @Autowired
    public RoleResourceAssembler(EntityLinks entityLinks) {
        this.entityLinks = entityLinks;
    }

    @Override
    public Resource<Role> toResource(Role entity) {
        final Resource<Role> resource = new Resource<>(entity);

        resource.add(entityLinks.linkToSingleResource(Role.class, entity.getTitle()).withSelfRel());
        resource.add(entityLinks.linkToSingleResource(Role.class, entity.getTitle()).withRel("role"));
        return resource;
    }
}
