package be.ordina.ordineo.controller;

import be.ordina.ordineo.model.Employee;
import be.ordina.ordineo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@ExposesResourceFor(Employee.class)
@RequestMapping(
        value = "/employees",
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE},
        consumes = {MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE}
)
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeResourceAssembler employeeResourceAssembler;

    @Autowired
    public EmployeeController(EmployeeService employeeService, EmployeeResourceAssembler employeeResourceAssembler) {
        this.employeeService = employeeService;
        this.employeeResourceAssembler = employeeResourceAssembler;
    }

    @GetMapping("")
    public HttpEntity<PagedResources<Resource<Employee>>> findAll(
            @RequestParam(value = "search", required = false) String search,
            Pageable pageable, PagedResourcesAssembler<Employee> assembler) {
        Page<Employee> employees = employeeService.findAll(Optional.ofNullable(search), pageable);
        if (pageable != null) {
            pageable.getPageNumber();
            pageable.getPageSize();
        }
        PagedResources<Resource<Employee>> pagedEmployeeResource = assembler.toResource(employees, employeeResourceAssembler);
        return new ResponseEntity<>(pagedEmployeeResource, HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity findByUsername(@PathVariable("username") String username) {
        Employee employee = employeeService.findByUsername(username);
        return new ResponseEntity<>(employeeResourceAssembler.toResource(employee), HttpStatus.OK);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity delete(@PathVariable("username") String username) {
        employeeService.delete(username);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{username}")
    public ResponseEntity update(@PathVariable("username") String username, @RequestBody @Valid Employee employee, HttpServletRequest request)
            throws URISyntaxException {
        Employee updatedEmployee = employeeService.update(username, employee);
        HttpHeaders httpHeaders = new HttpHeaders();
        URI uri = new URI(request.getRequestURL().append(updatedEmployee.getId()).toString());
        httpHeaders.setLocation(uri);
        return new ResponseEntity(httpHeaders, HttpStatus.NO_CONTENT);
    }

}
