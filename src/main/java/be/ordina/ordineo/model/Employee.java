package be.ordina.ordineo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
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
    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "USERNAME", length = 40)
    private String username;
    @Column(name = "PASSWORD", length = 40)
    private String password;
    @Column(name = "EMAIL", length = 50)
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

    @Override
    public UUID getId() {
        return uuid;
    }
}