package org.example;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;

    private String username;
    private String password;
}
