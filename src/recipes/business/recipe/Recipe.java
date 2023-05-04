package recipes.business.recipe;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import recipes.business.user.User;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Recipe {
    @JsonIgnore
    @Id
    @SequenceGenerator(
            name = "recipe_sequence",
            sequenceName = "recipe_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "recipe_sequence"
    )
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @ElementCollection
    @NotEmpty
    private List<String> ingredients;
    @ElementCollection
    @NotEmpty
    private List<String> directions;
    @NotBlank
    private String category;
    private LocalDateTime date;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

}
