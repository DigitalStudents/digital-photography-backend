package Backend.Producto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("v1/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @PostMapping
    public void createProducto(@RequestBody Producto camera) {
        productoService.CrearProducto(camera);
    }

    @GetMapping("/{id}")
        public Optional<Producto> getProducto(@PathVariable Long id) {
        return productoService.BuscarProducto(id);
    }

    @GetMapping
    public List<Producto> getAllProductos() {
        return productoService.TraerTodos();
    }

    @GetMapping("/random/{cantidad}")
    public List<Producto> obtenerProductosAleatorios(@PathVariable int cantidad) {
        return productoService.obtenerProductosAleatorios(cantidad);
    }
    @PutMapping("/{id}")
    public void updateProducto(@PathVariable Long id, @RequestBody Producto camera) {
        camera.setId(id);
        productoService.ModificarProducto(camera);
    }

    @DeleteMapping("/{id}")
    public void deleteProducto(@PathVariable Long id) {
        productoService.EliminarProducto(id);
    }
}
