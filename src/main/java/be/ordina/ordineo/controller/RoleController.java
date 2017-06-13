package be.ordina.ordineo.controller;

import be.ordina.ordineo.model.Role;
import be.ordina.ordineo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@ExposesResourceFor(Role.class)
@RequestMapping(
        value = "/roles",
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE},
        consumes = {MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE}
)
public class RoleController {
    private final RoleService roleService;
    private final RoleResourceAssembler roleResourceAssembler;

    @Autowired
    public RoleController(RoleService roleService, RoleResourceAssembler roleResourceAssembler) {
        this.roleService = roleService;
        this.roleResourceAssembler = roleResourceAssembler;
    }

    @GetMapping("")
    public HttpEntity<PagedResources<Resource<Role>>> findAll(Pageable pageable, PagedResourcesAssembler<Role> assembler) {
        final Page<Role> roles = roleService.findAll(pageable);
        final PagedResources<Resource<Role>> pagedRolesResource = assembler.toResource(roles, roleResourceAssembler);
        return new ResponseEntity<>(pagedRolesResource, HttpStatus.OK);
    }

    @DeleteMapping("/{title}")
    public ResponseEntity<Void> delete(@PathVariable("title") String title) {
        roleService.delete(title);
        return ResponseEntity.noContent().build();
    }
}
