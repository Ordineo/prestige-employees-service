package be.ordina.ordineo.Specification;

import be.ordina.ordineo.model.Employee;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created by shbe on 25/04/2017.
 */
public class EmployeeSpecification  implements Specification<Employee>{

    private SearchCriteria searchCriteria;

    public EmployeeSpecification(SearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

        if(searchCriteria.getOperation().equalsIgnoreCase(":")){
            if(root.get(searchCriteria.getKey()).getJavaType() == String.class){
                return criteriaBuilder.like(root.<String>get(searchCriteria.getKey()),"%" + searchCriteria.getValue() + "%");

            }else {
                return criteriaBuilder.equal(root.get(searchCriteria.getKey()),searchCriteria.getValue());
            }
        }
        return null;
    }
}
