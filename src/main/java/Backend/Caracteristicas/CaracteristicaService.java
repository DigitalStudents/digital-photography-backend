package Backend.Caracteristicas;

import java.util.List;
import java.util.Optional;

public interface CaracteristicaService {

    void CrearCaracteristica(Caracteristica caracteristica);

    Optional<Caracteristica> BuscarCaracteristica(Long id);

    List<Caracteristica> TraerTodos();

    void ModificarCaracteristica (Caracteristica caracteristica);

    void EliminarCaracteristica(Long id);
}
