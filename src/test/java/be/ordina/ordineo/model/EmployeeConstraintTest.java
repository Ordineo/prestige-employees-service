package be.ordina.ordineo.model;

import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import java.util.UUID;

import static be.ordina.ordineo.model.ConstraintMessage.*;
import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by shbe on 08/05/2017.
 */
public class EmployeeConstraintTest {
    private Employee employee;

    private Validator validator;
    @Before
    public void setUp() {
        employee = createEmployee();
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    @Test
    public void validateEmployee() {

        Set<ConstraintViolation<Employee>> constraintViolations =validator.validate(employee);
        assertThat(constraintViolations).isEmpty();
    }

    @Test
    public void testUsernameLength(){

        employee.setUsername("J");
        checkTheLengthOfTheField(employee ,USERNAME_LENGTH );
    }
    @Test
    public void testPasswordLenght(){
        employee.setPassword("de");
        checkTheLengthOfTheField(employee,PASSWORD_LENGTH);
    }
    @Test
    public void testFirstNameLength(){
        employee.setFirstName("J");
        checkTheLengthOfTheField(employee,FIRSTNAME_LENGTH);
    }
    @Test
    public void testLastNameLength(){
        employee.setLastName("L");
        checkTheLengthOfTheField(employee,LASTNAME_LENGTH);
    }
    @Test
    public void testUserNameIsNull(){
        employee.setUsername(null);
        checkIFTheFieldIsNull(employee , NOT_NULL);
    }
    @Test
    public void testFirstNameIsNull(){
        employee.setFirstName(null);
        checkIFTheFieldIsNull(employee , NOT_NULL);
    }
    @Test
    public void testLastNameIsNull(){
        employee.setLastName(null);
        checkTheLengthOfTheField(employee,NOT_NULL);
    }
    @Test
    public void testPasswordIsNull(){
        employee.setPassword(null);
        checkIFTheFieldIsNull(employee,NOT_NULL);
    }
    @Test
    public void testEmailIsNull(){
        employee.setEmail(null);
        checkIFTheFieldIsNull(employee,NOT_NULL);
    }
    @Test
    public void testUnitIsNull(){
        employee.setUnit(null);
        checkIFTheFieldIsNull(employee,NOT_NULL);
    }
    @Test
    public void testGenderIsNull(){
        employee.setGender(null);
        checkIFTheFieldIsNull(employee,NOT_NULL);
    }

    private Employee createEmployee() {
        Employee employee = new Employee();
        employee.setUsername("JLEFE1");
        employee.setFirstName("Joris");
        employee.setLastName("Lefever");
        employee.setUnit(Unit.JWORKS);
        employee.setPhone("0469696969");
        employee.setPassword("derp");
        employee.setGender(Gender.MALE);
        employee.setAvatar("http://d33v4339jhl8k0.cloudfront.net/docs/assets/528e78fee4b0865bc066be5a/images/52af1e8ce4b074ab9e98f0e0/file-mxuiNezRS5.jpg");
        employee.setUuid(UUID.randomUUID());
        employee.setEmail("joris.lefever@ordina.be");
        return employee;
    }
    private void checkTheLengthOfTheField(Employee employee ,String message){
        Set<ConstraintViolation<Employee>> constraintViolations = validator.validate(employee);
        assertThat(constraintViolations.size()).isEqualTo(1);
        assertThat(constraintViolations.iterator().next().getMessage()).isEqualTo(message);
    }
    private void checkIFTheFieldIsNull(Employee employee , String message) {
        Set<ConstraintViolation<Employee>> constraintViolations = validator.validate(employee);
        assertThat(constraintViolations.size()).isEqualTo(1);
        assertThat(constraintViolations.iterator().next().getMessage()).isEqualTo(message);
    }
}
