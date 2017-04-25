package be.ordina.ordineo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import java.util.Collection;
import java.util.UUID;

/**
 * Created by shbe on 13/04/2017.
 */


@NoArgsConstructor//should i set the access level attribute?????(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
@Table(name = "EMPLOYEES")
public class Employee implements Identifiable<UUID> {

    @Id
    @Column(name = "UUID")
    private UUID uuid;

    @Column(name = "USERNAME", length = 40)
    private String username;

    @Column(name = "PASSWORD", length = 64)
//    @Length(min = 6, message = "*Your password must have at least 6 characters")
//    @NotEmpty(message = "*Please provide your password")
//    @Transient
    private String password;

    @Column(name = "EMAIL", length = 50)
//    @Email(message = "*Please provide a valid Email")
//    @NotEmpty(message = "*Please provide an email")
    private String email;

    @Column(name = "FIRSTNAME", length = 60)
    private String firstName;
    @Column(name = "LASTNAME", length = 60)
    private String lastName;
    @Column(name = "AVATAR", length = 300)
    private String avatar;
    @Column(name = "PHONE", length = 15)
    private String phone;
    @Column(name = "UNIT", length = 15)
    private String unit;
    @Column(name = "GENDER", length = 20)
    private String gender;

    @ManyToMany
    @JoinTable(name = "ROLE_ASSIGNMENTS", joinColumns = {
            @JoinColumn(name = "EMPLOYEE_UUID")
    }, inverseJoinColumns = {
            @JoinColumn(name = "ROLE_ID")
    })
    private Collection<Role> roles;

    @Column(name = "ENABLED", columnDefinition = "Integer default '0' NOT NULL")
    private int enabled;
    @Column(name = "DELETED", columnDefinition = "Integer default '0' NOT NULL")
    private int deleted;

    public Employee(String username) {
        this.uuid = UUID.randomUUID();
        this.username = username;
        this.enabled = 0;
    }

    public Employee(String username, String password, String email, String firstName, String lastName, String avatar, String phone, String unit, String gender, Collection<Role> roles) {
        this.uuid = UUID.randomUUID();
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatar = avatar;
        this.phone = phone;
        this.unit = unit;
        this.gender = gender;
        this.roles = roles;
        this.enabled = 1;
    }

    @Override
    public UUID getId() {
        return uuid;
    }
}