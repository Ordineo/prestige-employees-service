package be.ordina.ordineo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.*;

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

    @NotNull
    @Column(name = "USERNAME", length = 40, unique = true)
    private String username;
    @NotNull
    @Column(name = "PASSWORD", length = 40)
    private String password;
    @NotNull
    @Column(name = "EMAIL", length = 50, unique = true)
    private String email;
    @NotNull
    @Column(name = "FIRSTNAME", length = 60)
    private String firstName;
    @NotNull
    @Column(name = "LASTNAME", length = 60)
    private String lastName;
    @Column(name = "AVATAR", length = 300)
    private String avatar;
    @Column(name = "PHONE", length = 15)
    private String phone;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Unit unit;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToMany(fetch = FetchType.LAZY)//, cascade = CascadeType.ALL)
    @JoinTable(name = "ROLE_ASSIGNMENTS", joinColumns = {
            @JoinColumn(name = "EMPLOYEES_ID", referencedColumnName = "UUID")
    }, inverseJoinColumns = {
            @JoinColumn(name = "ROLES_ID", referencedColumnName = "ID", nullable = false, updatable = false)
    })
    private List<Role> roles = new ArrayList<>();

    @Override
    public UUID getId() {
        return uuid;
    }

    /*
     The method annotated with @PrePersist in listener bean
     class is called before persisting data by entity manager persist() method.
     */
    @PrePersist
    public void generateUuid() {

        System.out.println("inside employee");
        if (getUuid() == null) {
            System.out.println("inside employee object null");
            setUuid(UUID.randomUUID());
        }
    }
}



