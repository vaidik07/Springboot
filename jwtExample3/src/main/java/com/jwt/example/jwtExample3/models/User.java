package com.jwt.example.jwtExample3.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    private String name;
    private String userId;
    private String email;
}
