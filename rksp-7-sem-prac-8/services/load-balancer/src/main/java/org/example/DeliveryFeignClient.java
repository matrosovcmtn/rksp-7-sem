package org.example;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "delivery-service", url = "${delivery.service.url}") // Совпадает с конфигами
public interface DeliveryFeignClient {

    @GetMapping("/api/delivery/{id}")
   ResponseEntity<DeliveryDTO> getDelivery(@PathVariable("id") Long id);

    @PostMapping("/api/delivery/create")
    ResponseEntity<Delivery> createDelivery(@RequestParam("pizzaId") Long pizzaId,
                                                   @RequestParam("userId") Long userId);
}
