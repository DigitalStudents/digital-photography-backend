package Backend.Inventory;

import Backend.Producto.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("v1/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/{productId}")
    public void addProductToInventory(@PathVariable Long productId, @RequestBody Map<String, Integer> request) {
        int initialStock = request.get("initialStock");
        Producto product = new Producto();
        product.setId(productId);

        inventoryService.addProductToInventory(product, initialStock);
    }

    @GetMapping("/{productId}")
    public int getStockForProduct(@PathVariable Long productId) {
        Producto product = new Producto();
        product.setId(productId);
        return inventoryService.getStockForProduct(product);
    }

    @PutMapping("/{productId}")
    public void updateStockForProduct(@PathVariable Long productId, @RequestParam int newStock) {
        Producto product = new Producto();
        product.setId(productId);
        inventoryService.updateStockForProduct(product, newStock);
    }

    @DeleteMapping("/{productId}")
    public void decreaseStockForProduct(@PathVariable Long productId, @RequestParam int quantity) {
        Producto product = new Producto();
        product.setId(productId);
        inventoryService.decreaseStockForProduct(product, quantity);
    }
}
