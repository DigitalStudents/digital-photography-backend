package PICamada4Equipo4.demo.CamaraDeFotos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CamaraDeFotosServiceImpl implements CamaraDeFotosService{

    @Autowired
    private CamaraDeFotosRepository camaraDeFotosRepository;

    @Override
    public void CrearCamaraDeFotos(CamaraDeFotos camaraDeFotos) {

        camaraDeFotosRepository.save(camaraDeFotos);
    }

    @Override
    public Optional<CamaraDeFotos> BuscarCamaraDeFotos(Long id) {
        return camaraDeFotosRepository.findById(id);
    }

    @Override
    public List<CamaraDeFotos> TraerTodos() {
        return camaraDeFotosRepository.findAll();
    }

    @Override
    public void ModificarCamaraDeFotos(CamaraDeFotos camaraDeFotos) {
        camaraDeFotosRepository.save(camaraDeFotos);
    }

    @Override
    public void EliminarCamaraDeFotos(Long id) {
        camaraDeFotosRepository.deleteById(id);
    }

}
