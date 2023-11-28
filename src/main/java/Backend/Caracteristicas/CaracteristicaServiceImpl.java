package Backend.Caracteristicas;

import Backend.exceptions.CaracteristicaNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CaracteristicaServiceImpl implements CaracteristicaService {

    @Autowired
    private CaracteristicaRepository caracteristicaRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(CaracteristicaServiceImpl.class);

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
        Optional<Caracteristica> optionalCaracteristica = caracteristicaRepository.findById(id);
        if (optionalCaracteristica.isPresent()) {
            try {
                caracteristicaRepository.deleteById(id);
            } catch (Exception e) {
                LOGGER.error("Error eliminando característica con id: {}", id, e);
                throw new RuntimeException("Error eliminando característica con id: " + id, e);
            }
        } else {
            throw new CaracteristicaNotFoundException("No se encontró una característica con id: " + id);
        }
    }
}