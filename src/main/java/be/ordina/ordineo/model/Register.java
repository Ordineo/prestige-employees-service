package be.ordina.ordineo.model;

import lombok.*;

/**
 * @author Maarten Casteels
 * @since 2017
 */
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class Register {

    private String username;
    private String password;

    public Register(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
