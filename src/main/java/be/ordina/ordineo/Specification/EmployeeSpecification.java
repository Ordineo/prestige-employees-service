package be.ordina.ordineo.Specification;

import be.ordina.ordineo.model.Employee;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created by shbe on 25/04/2017.
 * This search is case sensitive
 */
public class EmployeeSpecification  implements Specification<Employee>{

    private SearchCriteria searchCriteria;

    public EmployeeSpecification(SearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

     /*   criteriaQuery.multiselect(root.get("username"),root.get("email"),root.get("firtsName"),root.get("lastName"),root.get("avatar"),root.get("phone"),
                root.get("unit"),root.get("gender"),root.get("rooles"),root.get("uuid"));*/
        if(searchCriteria.getOperation().equalsIgnoreCase(":")){
            if(root.get(searchCriteria.getKey()).getJavaType() == String.class){
                return criteriaBuilder.like(root.<String>get(searchCriteria.getKey()),"%" + searchCriteria.getValue() + "%");

            }else {
                return criteriaBuilder.equal(root.get(searchCriteria.getKey()),searchCriteria.getValue());
            }
        }
        return null;
    }
    //question number 3 this http://localhost:8081/employees?search=lastName:Last,firstName:Kate it's based on the contact between front end and back end
    //if not how to avoid an exception for enum or other java types?
    //question number 2 can i here manipulate the criteria to get all the fields except for password or there is no way?
    //question number 1 other wise all 3 methods for get instead of query by name just putting the query above the repository method
    //since for three of them i am not supposed to give the password back
}
