package ru.vsu.cs.proskuryakov.coffeestrike.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Drink {

    private String id;

    @NotBlank
    private String name;

    @NotNull
    private Category category;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String imageLink;

    private String description;

    private String recipe;

    @UniqueElements
    private List<String> volumes;

    @UniqueElements
    private List<Ingredient> ingredients;

    private Integer cookingTime;

}
