package org.example;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "pizzaClient", url = "${pizza.service.url}")
public interface PizzaClient {
    @GetMapping("/api/pizza/{id}")
    Pizza getPizzaById(@PathVariable Long id);
}
