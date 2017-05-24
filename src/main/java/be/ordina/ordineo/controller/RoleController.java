package be.ordina.ordineo.controller;

import be.ordina.ordineo.model.Role;
import be.ordina.ordineo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

/**
 * Created by shbe on 26/04/2017.
 */

@RestController
@ExposesResourceFor(Role.class)//for entityLink
@RequestMapping("/roles")
public class RoleController {
    private final RoleService roleService;
    private final RoleResourceAssembler roleResourceAssembler;

    @Autowired
    public RoleController(RoleService roleService, RoleResourceAssembler roleResourceAssembler) {
        this.roleService = roleService;
        this.roleResourceAssembler = roleResourceAssembler;
    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = {"application/json", "application/hal+json"})
    public HttpEntity<PagedResources<Resource<Role>>> findAll(PagedResourcesAssembler<Role> assembler) {
        Page<Role> roles = roleService.findAll();
        PagedResources<Resource<Role>> pagedRoleeResource = assembler.toResource(roles, roleResourceAssembler);

        return new ResponseEntity<PagedResources<Resource<Role>>>(pagedRoleeResource, HttpStatus.OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = {"application/json", "application/hal+json"},
            produces = {"application/json", "application/hal+json"})
    public ResponseEntity<Void> save(@RequestBody @Valid Role role, UriComponentsBuilder ucBuilder) {
        roleService.save(role);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/roles/{id}").buildAndExpand((role.getId())).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{title}", method = RequestMethod.DELETE, produces = {"application/hal+json", "application/json"})
    public ResponseEntity<Void> delete(@PathVariable("title") String title) {
        roleService.delete(title);
        return ResponseEntity.noContent().build();
    }
}
