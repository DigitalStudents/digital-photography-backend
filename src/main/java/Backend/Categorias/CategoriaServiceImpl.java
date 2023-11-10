package Backend.Categorias;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class CategoriaServiceImpl implements CategoriaService{
    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public void CrearCategoria(Categoria categoria) {
        categoriaRepository.save(categoria);

    }

    @Override
    public Optional<Categoria> BuscarCategoria(Long id) {
        return categoriaRepository.findById(id);
    }

    @Override
    public List<Categoria> TraerTodos() {
        return categoriaRepository.findAll();
    }

    @Override
    public void ModificarCategoria(Categoria categoria) {
        categoriaRepository.save(categoria);

    }

    @Override
    public void EliminarCategoria(Long id) {
        categoriaRepository.deleteById(id);
    }
}
