package ru.vsu.cs.proskuryakov.coffeestrike.api.models;

import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.NotBlank;
import java.util.Map;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {

    @NotBlank
    private String name;

    @NotBlank
    private String unit;

    @UniqueElements
    private Map<String, Double> amount;

}
