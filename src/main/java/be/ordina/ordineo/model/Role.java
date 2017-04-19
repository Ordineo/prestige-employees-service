package be.ordina.ordineo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created by shbe on 12/04/2017.
 */
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ROLES")
public class Role implements Serializable {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TITLE", length = 100)
    private String title;

    @ManyToMany(mappedBy = "roles")
    private Collection<Employee> employees;

    public Role(String title) {
        this.title = title;
    }
}