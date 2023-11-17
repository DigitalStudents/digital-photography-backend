package Backend.Inventory;

import Backend.Producto.Producto;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("v1/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/{productId}")
    @Operation(summary = "Agrega un producto al inventario")
    public void addProductToInventory(@PathVariable Long productId, @RequestBody Map<String, Integer> request) {
        int initialStock = request.get("initialStock");
        Producto product = new Producto();
        product.setId(productId);

        inventoryService.addProductToInventory(product, initialStock);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Trae el Stock de un producto")
    public int getStockForProduct(@PathVariable Long productId) {
        Producto product = new Producto();
        product.setId(productId);
        return inventoryService.getStockForProduct(product);
    }

    @PutMapping("/{productId}")
    @Operation(summary = "Modifica el Stock de un producto")
    public void updateStockForProduct(@PathVariable Long productId, @RequestParam int newStock) {
        Producto product = new Producto();
        product.setId(productId);
        inventoryService.updateStockForProduct(product, newStock);
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "Decrementar el Stock de un producto")
    public void decreaseStockForProduct(@PathVariable Long productId, @RequestParam int quantity) {
        Producto product = new Producto();
        product.setId(productId);
        inventoryService.decreaseStockForProduct(product, quantity);
    }
}
