import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.home.dto.InventoryRequest;
import ru.home.dto.InventoryResponse;
import ru.home.model.Inventory;
import ru.home.repository.InventoryRepository;
import ru.home.service.InventoryService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryService inventoryService;

    @Test
    public void testCheckAndUpdateInventory_Success() {
        Inventory inventory = new Inventory();
        inventory.setProductId("1");
        inventory.setAvailableQuantity(10);

        when(inventoryRepository.findByProductId("1")).thenReturn(Optional.of(inventory));

        boolean result = inventoryService.checkAndUpdateInventory("1", 5);

        assertTrue(result);
        assertEquals(5, inventory.getAvailableQuantity());
        verify(inventoryRepository, times(1)).save(inventory);
    }

    @Test
    public void testCheckAndUpdateInventory_Failure() {
        when(inventoryRepository.findByProductId("1")).thenReturn(Optional.empty());

        boolean result = inventoryService.checkAndUpdateInventory("1", 5);

        assertFalse(result);
    }

    @Test
    public void testCreateOrUpdateInventory_NewInventory() {
        InventoryRequest request = new InventoryRequest("1", "Product1", 10);
        Inventory inventory = new Inventory();
        inventory.setProductId("1");
        inventory.setName("Product1");
        inventory.setAvailableQuantity(10);

        when(inventoryRepository.findByProductId("1")).thenReturn(Optional.empty());
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);

        InventoryResponse response = inventoryService.createOrUpdateInventory(request);

        assertNotNull(response);
        assertEquals("1", response.getProductId());
        assertEquals("Product1", response.getName());
        assertEquals(10, response.getAvailableQuantity());
    }

    @Test
    public void testGetAvailableQuantity() {
        Inventory inventory = new Inventory();
        inventory.setProductId("1");
        inventory.setAvailableQuantity(10);

        when(inventoryRepository.findByProductId("1")).thenReturn(Optional.of(inventory));

        Integer quantity = inventoryService.getAvailableQuantity("1");

        assertEquals(10, quantity);
    }
}