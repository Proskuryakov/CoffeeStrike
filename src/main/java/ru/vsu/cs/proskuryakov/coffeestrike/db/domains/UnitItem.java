package ru.vsu.cs.proskuryakov.coffeestrike.db.domains;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "units")
@ToString
public class UnitItem {

    @Id
    private String unitid;

    private String name;

}
