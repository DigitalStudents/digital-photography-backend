package Backend.Categorias;

import Backend.exceptions.CategoriaNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class CategoriaServiceImpl implements CategoriaService{
    @Autowired
    private CategoriaRepository categoriaRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaServiceImpl.class);


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
        Optional<Categoria> optionalCategoria = categoriaRepository.findById(id);
        if (optionalCategoria.isPresent()) {
            try {
                categoriaRepository.deleteById(id);
            } catch (Exception e) {
                LOGGER.error("Error eliminando categoría con id: {}", id, e);
                throw new RuntimeException("Error eliminando categoría con id: " + id, e);
            }
        } else {
            throw new CategoriaNotFoundException("No se encontró una categoría con id: " + id);
        }
    }
}
