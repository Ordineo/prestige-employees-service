package be.ordina.ordineo.service;

import be.ordina.ordineo.model.Employee;

/**
 * Created by SaFu on 18/04/2017.
 */
public interface EmployeeService {
    public Employee findEmployeeByUsername(String username);
    public void saveEmployee(Employee employee);
}
