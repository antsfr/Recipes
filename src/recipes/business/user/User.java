package recipes.business.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")        //default table name "user" is a keyword to DB
public class User implements CredentialsContainer {

    @JsonIgnore
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    @Column(name = "user_id")
    private Long id;
    @NotBlank(message = "email should not be blank")
    //@Email
    @Pattern(regexp = ".+@.+\\..+")
    private String email;
    @NotBlank(message = "password should not be blank")
    @Size(min = 8, message = "password should contain at least 8 characters")
    private String password;

    public void setPassword(String password) {
        this.password = password.trim();
    }

    @Override
    public void eraseCredentials() {

    }
}
