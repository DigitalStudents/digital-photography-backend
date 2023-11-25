package Backend.Producto;

import Backend.exceptions.ProductNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@RestController
@RequestMapping("v1/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Operation(summary = "Crea un Producto")
    @PostMapping
    public void createProducto(
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestBody Producto producto,
            @RequestParam(value = "caracteristicaIds", required = false) List<Long> caracteristicaIds,
            @RequestParam(value = "categoriaIds", required = false) List<Long> categoriaIds
    ) {
        if (images != null && !images.isEmpty()) {
            try {
                producto.uploadImagesToS3(images);
            } catch (IOException ignored) {
            }
        }

        productoService.CrearProducto(producto);

        if (caracteristicaIds != null) {
            productoService.agregarCaracteristicasAProducto(producto.getId(), caracteristicaIds);
        }

        if (categoriaIds != null) {
            productoService.agregarCategoriasAProducto(producto.getId(), categoriaIds);
        }
    }

    @Operation(summary = "Sube una imagen al bucket s3 (USAR POSTMAN)")
    @PostMapping("/{id}/subir-imagen")
    public void uploadImage(@PathVariable Long id,
                            @RequestParam("image") MultipartFile image) throws IOException {
        List<MultipartFile> imageFiles = Collections.singletonList(image);
        productoService.uploadImages(id, imageFiles);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Trae un producto por su ID")
    public ResponseEntity<?> getProducto(@PathVariable Long id) {
        try {
            Optional<Producto> optionalProducto = productoService.BuscarProducto(id);
            if (optionalProducto.isPresent()) {
                Producto producto = optionalProducto.get();
                return ResponseEntity.ok(producto);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró un producto con id: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error trayendo producto con id: " + id);
        }
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

    @Operation(summary = "Filtrar productos por categorías")
    @GetMapping("/filtrar-por-categorias")
    public List<Producto> filterProductosByCategorias(
            @RequestParam(value = "categoriaNombres", required = true) List<String> categoriaNombres
    ) {
        return productoService.filterProductosByCategorias(categoriaNombres);
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

    @Operation(summary = "Asocia categorías a un producto")
    @PostMapping("/{productoId}/asociar-categorias")
    public void addCategoriasToProducto(
            @PathVariable Long productoId,
            @RequestBody List<Long> categoriaIds
    ) {
        productoService.agregarCategoriasAProducto(productoId, categoriaIds);
    }

    @Operation(summary = "Borra un producto")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProducto(@PathVariable Long id) {
        try {

            Optional<Producto> optionalProducto = productoService.BuscarProducto(id);
            if (optionalProducto.isPresent()) {
                Producto producto = optionalProducto.get();


                if (producto.isDeleted()) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("El producto con id " + id + " ya está eliminado.");
                }

                productoService.EliminarProducto(id);

                return ResponseEntity.ok("Producto con id: " + id + " eliminado correctamente");
            } else {
                throw new ProductNotFoundException("No se encontró un producto con id: " + id);
            }
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error eliminando producto con id: " + id);
        }
    }

}
