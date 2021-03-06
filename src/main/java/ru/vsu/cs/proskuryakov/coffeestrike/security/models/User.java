package ru.vsu.cs.proskuryakov.coffeestrike.security.models;

import lombok.*;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String id;
    private String username;
    private String password;

}
