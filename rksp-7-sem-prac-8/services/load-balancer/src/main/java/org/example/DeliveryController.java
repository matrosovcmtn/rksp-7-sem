package org.example;

import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    @Autowired
    private DeliveryFeignClient deliveryFeignClient;

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryDTO> getDelivery(@PathVariable("id") Long id) {
        return deliveryFeignClient.getDelivery(id);
    }

    @PostMapping("/create")
    public ResponseEntity<Delivery> createDelivery(@RequestParam("pizzaId") Long pizzaId,
                                                   @RequestParam("userId") Long userId) {
        return deliveryFeignClient.createDelivery(pizzaId, userId);
    }
}
