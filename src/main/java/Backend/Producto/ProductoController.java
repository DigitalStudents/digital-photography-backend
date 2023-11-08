package Backend.Producto;

import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Crea un Producto")
    @PostMapping
    public void createProducto(
            @RequestBody Producto producto,
            @RequestParam(value = "caracteristicaIds", required = true) List<Long> caracteristicaIds
    ) {
        productoService.CrearProducto(producto);

        productoService.agregarCaracteristicasAProducto(producto.getId(), caracteristicaIds);
    }

    @Operation(summary = "Sube una imagen al bucket s3 (USAR POSTMAN)")
    @PostMapping("/{id}/subir-imagen")
    public void uploadImage(@PathVariable Long id,
                            @RequestParam("image") MultipartFile image) throws IOException  {
        productoService.uploadImage(id, image);
    }

    @Operation(summary = "Trae un producto por su ID")
    @GetMapping("/{id}")
        public Optional<Producto> getProducto(@PathVariable Long id) {
        return productoService.BuscarProducto(id);
    }

    @Operation(summary = "Trae todos los productos")
    @GetMapping
    public List<Producto> getAllProductos() {
        return productoService.TraerTodos();
    }

    @Operation(summary = "Trae todos los productos en orden aleatorio y los pagina")
    @GetMapping("/paginacion")
    public Page<Producto> getPagination(@RequestParam(value = "pagina", defaultValue = "0") int page,
                                        @RequestParam(value = "cantidad", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productoService.Paginados(pageable);
    }

    @Operation(summary = "Buscador de productos (paginado también)")
    @GetMapping("/buscador")
    public Page<Producto> searchProductos(
            @RequestParam("término") String searchTerm,
            @RequestParam(value = "página", defaultValue = "0") int page,
            @RequestParam(value = "cantidad", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return productoService.BuscarPorNombre(searchTerm, pageable);
    }

    @Operation(summary = "Modifica un producto")
    @PutMapping("/{id}")
    public void updateProducto(@PathVariable Long id, @RequestBody Producto producto) {
        producto.setId(id);
        productoService.ModificarProducto(producto);
    }

    @Operation(summary = "Asocia características a un producto")
    @PostMapping("/{productoId}/asociar-caracteristicas")
    public void addCaracteristicasToProducto(
            @PathVariable Long productoId,
            @RequestBody List<Long> caracteristicaIds
    ) {
        productoService.agregarCaracteristicasAProducto(productoId, caracteristicaIds);
    }

    @Operation(summary = "Borra un producto")
    @DeleteMapping("/{id}")
    public void deleteProducto(@PathVariable Long id) {
        productoService.EliminarProducto(id);
    }
}
