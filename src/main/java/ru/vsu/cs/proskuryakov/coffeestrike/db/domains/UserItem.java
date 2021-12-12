package ru.vsu.cs.proskuryakov.coffeestrike.db.domains;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
@ToString
public class UserItem {


    @Id
    private String userid;

    private String username;
    private String password;

}
