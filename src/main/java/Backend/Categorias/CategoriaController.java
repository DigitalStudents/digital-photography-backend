package Backend.Categorias;


import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("v1/categorias")
public class CategoriaController {

    @Autowired
    CategoriaService categoriaService;

    @Operation(summary = "Crea una categoría")
    @PostMapping
    public void createCategoria(@RequestBody Categoria categoria) {
        categoriaService.CrearCategoria(categoria);
    }

    @Operation(summary = "Trae una categoría por su ID")
    @GetMapping("/{id}")
    public Optional<Categoria> getCategoria(@PathVariable Long id) {
        return categoriaService.BuscarCategoria(id);
    }

    @Operation(summary = "Trae todas las categorías")
    @GetMapping
    public List<Categoria> getAllCategorias() {
        return categoriaService.TraerTodos();
    }

    @Operation(summary = "Modifica una categoría")
    @PutMapping("/{id}")
    public void updateCategoria(@PathVariable Long id, @RequestBody Categoria categoria) {
        categoria.setId(id);
        categoriaService.ModificarCategoria(categoria);
    }

    @Operation(summary = "Borra una categoría")
    @DeleteMapping("/{id}")
    public void deleteCategoria(@PathVariable Long id) {
        categoriaService.EliminarCategoria(id);
    }
}
