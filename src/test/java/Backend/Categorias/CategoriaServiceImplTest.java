package Backend.Categorias;

import Backend.Caracteristicas.Caracteristica;
import Backend.Caracteristicas.CaracteristicaRepository;
import Backend.Caracteristicas.CaracteristicaServiceImpl;
import Backend.Producto.Producto;
import Backend.exceptions.CaracteristicaNotFoundException;
import Backend.exceptions.CategoriaNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoriaServiceImplTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaServiceImpl categoriaServiceImpl;

    private Categoria categoria;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setDescripcion("camaras");
        categoria.setProductos(new ArrayList<Producto>());

    }

    @Test
    void crearCategoria() {
        categoriaServiceImpl.CrearCategoria(categoria);

        Mockito.verify(categoriaRepository, Mockito.times(1)).save(categoria);
    }

    @Test
    void buscarCategoria() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        Optional<Categoria> result = categoriaServiceImpl.BuscarCategoria(1L);
        assertTrue(result.isPresent(),"categoria encontrada");
    }

    @Test
    void buscarCaracteristicaError() {

        when(categoriaRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<Categoria> result = categoriaServiceImpl.BuscarCategoria(1L);
        assertFalse(result.isPresent(), "categoria no encontrada");
    }

    @Test
    void traerTodos() {
        when(categoriaRepository.findAll()).thenReturn(List.of(categoria));

        List<Categoria>categorias= categoriaServiceImpl.TraerTodos();
        assertEquals(List.of(categoria),categorias);
    }

    @Test
    void modificarCategoria() {
        categoriaServiceImpl.ModificarCategoria(categoria);

        Mockito.verify(categoriaRepository, Mockito.times(1)).save(categoria);
    }

    @Test
    void eliminarCategoria() {
        when(categoriaRepository.findById(categoria.getId())).thenReturn(Optional.of(categoria));

        categoriaServiceImpl.EliminarCategoria( categoria.getId());

        verify(categoriaRepository, times(1)).deleteById(categoria.getId());

    }

    @Test
    void eliminarCaracteristica_CaracteristicaNoEncontrada() {

        when(categoriaRepository.findById(categoria.getId())).thenReturn(Optional.empty());

        CategoriaNotFoundException exception = assertThrows(
                CategoriaNotFoundException.class,
                () -> categoriaServiceImpl.EliminarCategoria(categoria.getId())
        );

        assertEquals("No se encontró una categoría con id: " + categoria.getId(), exception.getMessage());
        verify(categoriaRepository, never()).deleteById(categoria.getId());
    }
}