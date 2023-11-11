package Backend.Caracteristicas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class CaracteristicaServiceImpl implements CaracteristicaService {

    @Autowired
    private CaracteristicaRepository caracteristicaRepository;

    @Override
    public void CrearCaracteristica(Caracteristica caracteristica) {
        caracteristicaRepository.save(caracteristica);

    }

    @Override
    public Optional<Caracteristica> BuscarCaracteristica(Long id) {
        return caracteristicaRepository.findById(id);
    }

    @Override
    public List<Caracteristica> TraerTodos() {
        return caracteristicaRepository.findAll();
    }

    @Override
    public void ModificarCaracteristica(Caracteristica caracteristica) {
        caracteristicaRepository.save(caracteristica);

    }

    @Override
    public void EliminarCaracteristica(Long id) {
        caracteristicaRepository.deleteById(id);
    }
}
