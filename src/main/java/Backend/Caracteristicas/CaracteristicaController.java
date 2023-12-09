package Backend.Caracteristicas;

import Backend.exceptions.CaracteristicaNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("v1/caracteristica")
public class CaracteristicaController {

    @Autowired
    CaracteristicaService caracteristicaService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CaracteristicaController.class);

    @Operation(summary = "Crea una característica")
    @PostMapping
    public ResponseEntity<?> createCaracteristica(@RequestBody Caracteristica caracteristica) {
        try {
            caracteristicaService.CrearCaracteristica(caracteristica);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creando característica");
        }
    }

    @Operation(summary = "Trae una característica por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getCaracteristica(@PathVariable Long id) {
        try {
            Optional<Caracteristica> caracteristica = caracteristicaService.BuscarCaracteristica(id);
            if (caracteristica.isPresent()) {
                return ResponseEntity.ok(caracteristica.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró una característica con id: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error trayendo característica con id: " + id);
        }
    }

    @Operation(summary = "Trae todas las características")
    @GetMapping
    public ResponseEntity<?> getAllCaracteristicas() {
        try {
            List<Caracteristica> caracteristicas = caracteristicaService.TraerTodos();
            return ResponseEntity.ok(caracteristicas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error trayendo todas las características");
        }
    }

    @Operation(summary = "Modifica una característica")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCaracteristica(@PathVariable Long id, @RequestBody Caracteristica caracteristica) {
        try {
            caracteristica.setId(id);
            caracteristicaService.ModificarCaracteristica(caracteristica);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error modificando característica con id: " + id);
        }
    }

    @Operation(summary = "Borra una característica")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCaracteristica(@PathVariable Long id) {
        try {
            caracteristicaService.EliminarCaracteristica(id);
            return ResponseEntity.ok("Característica con id: " + id + " eliminada correctamente");
        } catch (CaracteristicaNotFoundException e) {
            LOGGER.warn("No se encontró una característica con id: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró una característica con id: " + id);
        } catch (RuntimeException e) {
            LOGGER.error("Error eliminando característica con id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error eliminando característica con id: " + id);
        }
    }
}