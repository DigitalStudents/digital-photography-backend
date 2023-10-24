package Backend.Inventory;

import Backend.Producto.Producto;

public interface InventoryService {

    void addProductToInventory(Producto product, int initialStock);

    int getStockForProduct(Producto product);

    void updateStockForProduct(Producto product, int newStock);

    void decreaseStockForProduct(Producto product, int quantity);
}