package Backend.Producto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @PostMapping("/{id}/upload-image")
    public void uploadImage(@PathVariable Long id, @RequestParam("image") MultipartFile image) throws IOException {
        productoService.uploadImage(id, image);
    }

    @GetMapping("/{id}")
        public Optional<Producto> getProducto(@PathVariable Long id) {
        return productoService.BuscarProducto(id);
    }

    @GetMapping
    public List<Producto> getAllProductos() {
        return productoService.TraerTodos();
    }

    @GetMapping("/paginacion")
    public Page<Producto> getPagination(@RequestParam(value = "page", defaultValue = "0") int page,
                                          @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productoService.Paginados(pageable);
    }

    @GetMapping("/search")
    public Page<Producto> searchProductos(
            @RequestParam("term") String searchTerm,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return productoService.BuscarPorNombre(searchTerm, pageable);
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
