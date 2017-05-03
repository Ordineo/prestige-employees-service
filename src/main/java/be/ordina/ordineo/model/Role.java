package be.ordina.ordineo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.Identifiable;


import javax.persistence.*;
import java.util.*;

/**
 * Created by shbe on 12/04/2017.
 */
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ROLES" ,uniqueConstraints={
        @UniqueConstraint(columnNames = "ID"),
        @UniqueConstraint(columnNames = "TITLE")})
public class Role implements Identifiable<Long> {
    @Id
    @Column(name = "ID" , unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TITLE", length = 100, unique = true , nullable = false)
    private String title;

    @ManyToMany(mappedBy = "roles")
    private List<Employee> employees = new ArrayList<>();

    public Role(String title) {
        this.title = title;
    }

}
