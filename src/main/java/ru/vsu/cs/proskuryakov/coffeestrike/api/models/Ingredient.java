package ru.vsu.cs.proskuryakov.coffeestrike.api.models;

import lombok.*;

import java.util.Map;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {

    private String name;

    private String unit;

    private Map<String, Double> amount;

}
