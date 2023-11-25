package Backend.Categorias;


import Backend.exceptions.CategoriaNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
@RestController
@RequestMapping("v1/categorias")
public class CategoriaController {

    @Autowired
    CategoriaService categoriaService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaController.class);


    @Operation(summary = "Crea una categoría")
    @PostMapping
    public ResponseEntity<?> createCategoria(@RequestBody Categoria categoria) {
        try {
            categoriaService.CrearCategoria(categoria);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creando categoría");
        }
    }

    @Operation(summary = "Trae una categoría por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoria(@PathVariable Long id) {
        try {
            Optional<Categoria> categoria = categoriaService.BuscarCategoria(id);
            if (categoria.isPresent()) {
                return ResponseEntity.ok(categoria.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró una categoría con id: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error trayendo categoría con id: " + id);
        }
    }

    @Operation(summary = "Trae todas las categorías")
    @GetMapping
    public ResponseEntity<?> getAllCategorias() {
        try {
            List<Categoria> categorias = categoriaService.TraerTodos();
            return ResponseEntity.ok(categorias);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error trayendo todas las categorías");
        }
    }

    @Operation(summary = "Modifica una categoría")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategoria(@PathVariable Long id, @RequestBody Categoria categoria) {
        try {
            categoria.setId(id);
            categoriaService.ModificarCategoria(categoria);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error modificando categoría con id: " + id);
        }
    }

    @Operation(summary = "Borra una categoría")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategoria(@PathVariable Long id) {
        try {
            categoriaService.EliminarCategoria(id);
            return ResponseEntity.ok("Categoria con id: " + id + " eliminada correctamente");
        } catch (CategoriaNotFoundException e) {
            LOGGER.warn("No se encontró una categoría con id: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró una categoría con id: " + id);
        } catch (Exception e) {
            LOGGER.error("Error eliminando categoría con id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error eliminando categoría con id: " + id);
        }
    }
}