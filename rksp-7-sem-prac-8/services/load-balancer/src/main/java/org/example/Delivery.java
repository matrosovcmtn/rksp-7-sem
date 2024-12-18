package org.example;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Delivery {

    private Long id;

    private Long userId;

    private Long pizzaId;
}
