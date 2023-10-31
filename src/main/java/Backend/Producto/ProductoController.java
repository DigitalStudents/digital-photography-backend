package Backend.Producto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("v1/productos")
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

    @GetMapping("/pagination")
    public Page<Producto> getPagination(@RequestParam(value = "page", defaultValue = "0") int page,
                                          @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productoService.Paginados(pageable);
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
