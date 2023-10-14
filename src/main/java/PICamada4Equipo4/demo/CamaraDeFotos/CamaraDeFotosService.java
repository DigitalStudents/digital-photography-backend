package PICamada4Equipo4.demo.CamaraDeFotos;

import java.util.List;
import java.util.Optional;

public interface CamaraDeFotosService {

     void CrearCamaraDeFotos (CamaraDeFotos camaraDeFotos);
     Optional<CamaraDeFotos> BuscarCamaraDeFotos(Long id);
     List<CamaraDeFotos> TraerTodos();
     void ModificarCamaraDeFotos (CamaraDeFotos camaraDeFotos);
     void EliminarCamaraDeFotos (Long id);

}
