package be.ordina.ordineo.controller;

import be.ordina.ordineo.model.Employee;
import be.ordina.ordineo.model.Role;
import be.ordina.ordineo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.PagedResources.PageMetadata;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * Created by shbe on 13/04/2017.
 */


@RestController
@ExposesResourceFor(Employee.class)//for entityLink
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeResourceAssembler employeeResourceAssembler;

    @Autowired
    public EmployeeController (EmployeeService employeeService,EmployeeResourceAssembler employeeResourceAssembler){
        this.employeeService = employeeService;
        this.employeeResourceAssembler = employeeResourceAssembler;
    }

    @RequestMapping(value="/{username}" , method =  RequestMethod.GET,produces = {"application/json", "application/hal+json"})
    public ResponseEntity findByUsername(@PathVariable("username") String username){
         Employee employee = employeeService.findByUsername(username);
         return new ResponseEntity<>(employeeResourceAssembler.toResource(employee),HttpStatus.OK);
    }
    @RequestMapping(value="/search/findByRole",method = RequestMethod.GET,produces = {"application/hal+json", "application/json"})
    public HttpEntity<PagedResources<Resource<Employee>>> findByRole(@RequestParam("roleTitle") @Valid String roleTitle,PagedResourcesAssembler<Employee> assembler){
        Page<Employee> employees = employeeService.findByRole(roleTitle);
        PagedResources<Resource<Employee>> pagedEmployeeResource = assembler.toResource(employees,employeeResourceAssembler);
        return new ResponseEntity<PagedResources<Resource<Employee>>>(pagedEmployeeResource,HttpStatus.OK);
    }

    @RequestMapping(value="" ,method =RequestMethod.GET,produces = {"application/json", "application/hal+json"})
    public HttpEntity<PagedResources<Resource<Employee>>> findAll(@RequestParam("search") Optional<String> search,
                                                                  Pageable pageable, PagedResourcesAssembler<Employee> assembler){
       // Page<Employee>  employees = employeeService.findAll(Optional<String> query, Pageable pageable);
         Page<Employee>  employees = employeeService.findAll(search,pageable);
        if(pageable != null ){
            pageable.getPageNumber();
            pageable.getPageSize();
        }
        PagedResources<Resource<Employee>> pagedEmployeeResource = assembler.toResource(employees,employeeResourceAssembler);
        return new ResponseEntity<PagedResources<Resource<Employee>>>(pagedEmployeeResource,HttpStatus.OK);
    }


    @RequestMapping(value = "/{username}",method = RequestMethod.DELETE , produces = {"application/hal+json", "application/json"})
    public ResponseEntity delete(@PathVariable("username") String username){
        employeeService.delete(username);
        // HttpHeaders httpHeaders = new HttpHeaders();
        // return new ResponseEntity<>(httpHeaders,HttpStatus.NO_CONTENT);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value="/",method=RequestMethod.POST, consumes = {"application/json","application/hal+json"},
            produces = {"application/json","application/hal+json"})//question are we gonna use hibernate validator ---> then i can use @Valid or not??,,
    public ResponseEntity<Void> save(@RequestBody @Valid Employee employee, UriComponentsBuilder ucBuilder  ){
        if(!employee.getRoles().isEmpty()){
            employee = employeeService.saveWithRoles(employee);
        }else {
            employee = employeeService.save(employee);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/employees/{uuid}").buildAndExpand((employee.getId())).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }


    @RequestMapping(value="/{username}" , method = RequestMethod.PUT ,consumes = {"application/hal+json", "application/json"}, produces = {"application/hal+json", "application/json"})
    public ResponseEntity update(@PathVariable("username") String username , @RequestBody @Valid Employee employee, HttpServletRequest request)
            throws URISyntaxException {
        Employee updatedEmployee = employeeService.update(username , employee);
        HttpHeaders httpHeaders = new HttpHeaders();
        URI uri = new URI(request.getRequestURL().append(updatedEmployee.getId()).toString());
        httpHeaders.setLocation(uri);
        return new ResponseEntity( httpHeaders,HttpStatus.CREATED);
    }

}
