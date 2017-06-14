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
    private String confirmPassword;

    public Register(String username, String password, String confirmPassword) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
}
