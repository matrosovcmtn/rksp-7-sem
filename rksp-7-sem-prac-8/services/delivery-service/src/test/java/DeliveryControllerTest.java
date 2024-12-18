import jakarta.ws.rs.NotFoundException;
import org.example.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeliveryControllerTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private UserClient userClient;

    @Mock
    private PizzaClient pizzaClient;

    @InjectMocks
    private DeliveryController deliveryController;

    private Delivery delivery;
    private Pizza pizza;
    private User user;

    @BeforeEach
    void setUp() {
        delivery = Delivery.builder()
                .id(1L)
                .userId(10L)
                .pizzaId(20L)
                .build();

        pizza = Pizza.builder()
                .id(20L)
                .name("Margherita")
                .build();

        user = User.builder()
                .id(10L)
                .username("john_doe")
                .password("password123")
                .build();
    }

    @Test
    void getDelivery_shouldReturnDeliveryDTO_whenDeliveryExists() {
        when(deliveryRepository.findById(1L)).thenReturn(Optional.of(delivery));
        when(pizzaClient.getPizzaById(20L)).thenReturn(pizza);
        when(userClient.getUserById(10L)).thenReturn(user);

        ResponseEntity<DeliveryDTO> response = deliveryController.getDelivery(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Margherita", response.getBody().getPizza().getName());
        assertEquals("john_doe", response.getBody().getUser().getUsername());

        verify(deliveryRepository, times(1)).findById(1L);
        verify(pizzaClient, times(1)).getPizzaById(20L);
        verify(userClient, times(1)).getUserById(10L);
    }

    @Test
    void getDelivery_shouldReturnNotFound_whenDeliveryDoesNotExist() {
        when(deliveryRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<DeliveryDTO> response = deliveryController.getDelivery(1L);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());

        verify(deliveryRepository, times(1)).findById(1L);
        verifyNoInteractions(pizzaClient, userClient);
    }

    @Test
    void createDelivery_shouldReturnCreatedDelivery_whenValidData() {
        when(pizzaClient.getPizzaById(20L)).thenReturn(pizza);
        when(userClient.getUserById(10L)).thenReturn(user);
        when(deliveryRepository.save(any(Delivery.class))).thenReturn(delivery);

        ResponseEntity<Delivery> response = deliveryController.createDelivery(20L, 10L);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals(10L, response.getBody().getUserId());
        assertEquals(20L, response.getBody().getPizzaId());

        verify(pizzaClient, times(1)).getPizzaById(20L);
        verify(userClient, times(1)).getUserById(10L);
        verify(deliveryRepository, times(1)).save(any(Delivery.class));
    }


}
