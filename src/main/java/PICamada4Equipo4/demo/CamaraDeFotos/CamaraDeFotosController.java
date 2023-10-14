package PICamada4Equipo4.demo.CamaraDeFotos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("v1/camaras")
public class CamaraDeFotosController {

    @Autowired
    private CamaraDeFotosService camaraDeFotosService;

    @PostMapping
    public void createCamera(@RequestBody CamaraDeFotos camera) {
        camaraDeFotosService.CrearCamaraDeFotos(camera);
    }

    @GetMapping("/{id}")
    public Optional<CamaraDeFotos> getCamera(@PathVariable Long id) {
        return camaraDeFotosService.BuscarCamaraDeFotos(id);
    }

    @GetMapping
    public List<CamaraDeFotos> getAllCameras() {
        return camaraDeFotosService.TraerTodos();
    }

    @PutMapping("/{id}")
    public void updateCamera(@PathVariable Long id, @RequestBody CamaraDeFotos camera) {
        camera.setId(id);
        camaraDeFotosService.ModificarCamaraDeFotos(camera);
    }

    @DeleteMapping("/{id}")
    public void deleteCamera(@PathVariable Long id) {
        camaraDeFotosService.EliminarCamaraDeFotos(id);
    }
}
