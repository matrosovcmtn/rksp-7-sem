package org.example;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pizza")
@RequiredArgsConstructor
public class PizzaController {

    private final PizzaRepository pizzaRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Pizza>> getPizza(@PathVariable("id") Long id) {
        Optional<Pizza> pizza = pizzaRepository.findById(id);
        if (pizza.isPresent()) {
            return ResponseEntity.ok(pizza);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Pizza> createPizza(@RequestParam("name") String name) {
        Pizza newPizza = Pizza.builder()
                .name(name)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(pizzaRepository.save(newPizza));
    }
}
