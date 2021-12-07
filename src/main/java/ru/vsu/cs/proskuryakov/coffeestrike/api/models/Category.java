package ru.vsu.cs.proskuryakov.coffeestrike.api.models;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    private String id;

    @NotBlank
    private String name;

    private String imageLink;

}
