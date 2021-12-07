package ru.vsu.cs.proskuryakov.coffeestrike.db.domains;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.vsu.cs.proskuryakov.coffeestrike.api.models.Ingredient;

import java.util.List;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "drinks")
@ToString
public class DrinkItem {

    @Id
    private String drinkid;

    private String name;

    @DBRef
    private CategoryItem categoryItem;

    private String imageLink;

    private String description;

    private String recipe;

    private List<String> volumes;

    private List<Ingredient> ingredients;

    private Integer cookingTime;

}
