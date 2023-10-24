package Backend.Inventory;

import Backend.Producto.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public void addProductToInventory(Producto product, int initialStock) {
        Inventory item = new Inventory();
        item.setProducto(product);
        item.setCantidad(initialStock);
        inventoryRepository.save(item);
    }

    public int getStockForProduct(Producto product) {
        Inventory inventory = inventoryRepository.findByProducto(product);
        return inventory != null ? inventory.getCantidad() : 0;
    }

    public void updateStockForProduct(Producto product, int newStock) {
        Inventory item = inventoryRepository.findByProducto(product);
        if (item != null) {
            item.setCantidad(newStock);
            inventoryRepository.save(item);
        }
    }

    public void decreaseStockForProduct(Producto product, int quantity) {
        int currentStock = getStockForProduct(product);
        if (currentStock >= quantity) {
            updateStockForProduct(product, currentStock - quantity);
        } else {
            throw new RuntimeException("Stock insuficiente para este producto.");
        }
    }
}
