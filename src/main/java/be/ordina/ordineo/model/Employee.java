package be.ordina.ordineo.model;

import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@Entity
@EqualsAndHashCode(exclude = {"roles"})
@ToString(exclude = {"roles"})
@Table(name = "EMPLOYEES", uniqueConstraints = {
        @UniqueConstraint(columnNames = "USERNAME"),
        @UniqueConstraint(columnNames = "EMAIL")
})
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
    @Column(name = "PASSWORD", length = 60)
    private String password;
    @Column(name = "EMAIL", length = 50, unique = true)
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

    @ManyToMany(fetch = FetchType.LAZY)//, cascade = CascadeType.ALL)
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

    public Employee(String username) {
        this.uuid = UUID.randomUUID();
        this.username = username;
        this.enabled = 1;
    }

    public Employee(String username, String password, String email, String firstName, String lastName, String avatar, String phone, Unit unit, Gender gender, List<Role> roles) {
        this.uuid = UUID.randomUUID();
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatar = avatar;
        this.phone = phone;
        this.gender = gender;
        this.unit = unit;
        this.roles = roles;
        this.enabled = 1;
    }

    @PrePersist
    public void generateUuid() {
        if (getUuid() == null) {
            setUuid(UUID.randomUUID());
        }
    }

    @Override
    public UUID getId() {
        return uuid;
    }
}