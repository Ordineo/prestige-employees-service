package be.ordina.ordineo.Specification;

import be.ordina.ordineo.model.Employee;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import java.util.ArrayList;
import java.util.List;


public class EmployeeSpecificationsBuilder {
    private final List<SearchCriteria> params;

    public EmployeeSpecificationsBuilder() {
        this.params = new ArrayList<SearchCriteria>();
    }

    public EmployeeSpecificationsBuilder with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public Specification<Employee> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification<Employee>> specs = new ArrayList<Specification<Employee>>();
        for (SearchCriteria param : params) {
            specs.add(new EmployeeSpecification(param));
        }

        Specification<Employee> result = specs.get(0);
        for (int i = 1; i < specs.size(); i++) {
            result = Specifications.where(result).and(specs.get(i));
        }
        return result;
    }
}
