package Backend.Caracteristicas;


import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("v1/caracteristica")
public class CaracteristicaController {
    @Autowired
    CaracteristicaService caracteristicaService;

    @Operation(summary = "Crea una característica")
    @PostMapping
    public void createProducto(@RequestBody Caracteristica caracteristica) {
        caracteristicaService.CrearCaracteristica(caracteristica);
    }

    @Operation(summary = "Trae una característica por su ID")
    @GetMapping("/{id}")
    public Optional<Caracteristica> getCaracteristica(@PathVariable Long id) {
        return caracteristicaService.BuscarCaracteristica(id);
    }

    @Operation(summary = "Trae todas las características")
    @GetMapping
    public List<Caracteristica> getAllCaracteristicas() {
        return caracteristicaService.TraerTodos();
    }

    @Operation(summary = "Modifica una característica")
    @PutMapping("/{id}")
    public void updateCaracteristica(@PathVariable Long id, @RequestBody Caracteristica caracteristica) {
        caracteristica.setId(id);
        caracteristicaService.ModificarCaracteristica(caracteristica);
    }

    @Operation(summary = "Borra una característica")
    @DeleteMapping("/{id}")
    public void deleteCaracteristica(@PathVariable Long id) {
        caracteristicaService.EliminarCaracteristica(id);
    }

}
