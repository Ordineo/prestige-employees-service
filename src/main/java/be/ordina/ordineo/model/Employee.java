package be.ordina.ordineo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.hibernate.validator.constraints.Email;
import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

import static be.ordina.ordineo.model.ConstraintMessage.*;

/**
 * Created by shbe on 13/04/2017.
 */


@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "EMPLOYEES", uniqueConstraints = {
        @UniqueConstraint(columnNames = "USERNAME"),
        @UniqueConstraint(columnNames = "EMAIL")})
public class Employee implements Identifiable<UUID> {

    @Id
    @Column(name = "UUID")
    @Type(type = "uuid-char")
    private UUID uuid;

    @Column(name = "GITHUB_ID")
    private int githubId;

    @NotNull(message = NOT_NULL)
    @Column(name = "USERNAME",  unique = true)
    @Size(min = 2   ,max = 30 , message = USERNAME_LENGTH)
    private String username;
    @Column(name = "PASSWORD", length = 60)
    private String password;
    @Column(name = "EMAIL", length = 50, unique = true)
    @Email
    private String email;
    @Column(name = "FIRSTNAME", length = 60)
    private String firstName;
    @Column(name = "LASTNAME", length = 60)
    private String lastName;
    @Column(name = "AVATAR", length = 300)
    private String avatar;
    @Column(name = "PHONE", length = 15)
    private String phone;

    @Enumerated(EnumType.STRING)
    private Unit unit;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "ROLE_ASSIGNMENTS", joinColumns = {
            @JoinColumn(name = "EMPLOYEE_UUID", referencedColumnName = "UUID")
    }, inverseJoinColumns = {
            @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID", nullable = false, updatable = false)
    })
    private List<Role> roles = new ArrayList<>();

    @Column(name = "ENABLED", columnDefinition = "Integer default '0' NOT NULL")
    private int enabled;
    @Column(name = "DELETED", columnDefinition = "Integer default '0' NOT NULL")
    private int deleted;

    @Override
    public UUID getId() {
        return uuid;
    }

    public Employee(String username) {
        this.uuid = UUID.randomUUID();
        this.username = username;
        this.enabled = 0;
    }

    public Employee(String username, String password, String email, String firstName, String lastName, String avatar, String phone, String unit, String gender, List<Role> roles) {
        this.uuid = UUID.randomUUID();
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatar = avatar;
        this.phone = phone;
//        this.unit = unit;
//        this.gender = gender;
        this.roles = roles;
        this.enabled = 1;
    }

    /*
     The method annotated with @PrePersist in listener bean
     class is called before persisting data by entity manager persist() method.
     */
    @PrePersist
    public void generateUuid() {

        if (getUuid() == null) {
            setUuid(UUID.randomUUID());
        }
    }

    @Override
    public String toString() {
        return "Employee{" +
                "uuid=" + uuid +
                ", githubId=" + githubId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", phone='" + phone + '\'' +
                ", unit=" + unit +
                ", gender=" + gender +
                ", roles=" + roles +
                ", enabled=" + enabled +
                ", deleted=" + deleted +
                '}';
    }
}



