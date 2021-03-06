package be.ordina.ordineo.model.projection;

import be.ordina.ordineo.model.Employee;
import be.ordina.ordineo.model.Gender;
import be.ordina.ordineo.model.Unit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "employeeView", types = {Employee.class})
public interface EmployeeView {

    String getUsername();

    String getFirstName();

    String getLastName();

    String getEmail();

    String getAvatar();

    Unit getUnit();

    Gender getGender();

    String getPhone();

}
