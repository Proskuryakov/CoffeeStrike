package ru.vsu.cs.proskuryakov.coffeestrike.api.models;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private String id;

    @NotBlank
    private String name;

}
