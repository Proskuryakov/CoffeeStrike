package ru.vsu.cs.proskuryakov.coffeestrike.db.domains;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "products")
@ToString
public class ProductItem {

    @Id
    private String productid;

    private String name;

}
