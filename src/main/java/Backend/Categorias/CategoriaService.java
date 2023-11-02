package Backend.Categorias;

import java.util.List;
import java.util.Optional;

public interface CategoriaService {

    void CrearCategoria(Categoria categoria);

    Optional<Categoria> BuscarCategoria(Long id);

    List<Categoria> TraerTodos();

    void ModificarCategoria (Categoria categoria);

    void EliminarCategoria(Long id);
}
