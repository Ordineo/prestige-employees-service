package be.ordina.ordineo.model;

import lombok.*;
import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"employees"})
@EqualsAndHashCode(exclude = {"employees"})
@Entity
@Table(name = "ROLES", uniqueConstraints = {
        @UniqueConstraint(columnNames = "ID"),
        @UniqueConstraint(columnNames = "TITLE")})
public class Role implements Identifiable<Long> {
    @Id
    @Column(name = "ID", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TITLE", length = 100, unique = true, nullable = false)
    private String title;

    @ManyToMany(mappedBy = "roles")
    private List<Employee> employees = new ArrayList<>();

    public Role(String title) {
        this.title = title;
    }

}