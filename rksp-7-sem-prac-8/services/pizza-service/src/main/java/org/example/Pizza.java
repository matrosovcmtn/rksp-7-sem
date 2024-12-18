package org.example;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "pizza")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pizza {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private int cost;

}
