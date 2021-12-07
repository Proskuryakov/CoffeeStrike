package ru.vsu.cs.proskuryakov.coffeestrike.db.domains;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "categories")
@ToString
public class CategoryItem {

    @Id
    private String categoryid;

    private String name;

    private String imageLink;

}
