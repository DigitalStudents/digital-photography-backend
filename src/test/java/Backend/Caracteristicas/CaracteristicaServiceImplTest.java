package Backend.Caracteristicas;
import Backend.Producto.Producto;
import Backend.exceptions.CaracteristicaNotFoundException;
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

class CaracteristicaServiceImplTest {

    @Mock
    private CaracteristicaRepository caracteristicaRepository;

    @InjectMocks
    private CaracteristicaServiceImpl caracteristicaServiceImpl;

    private Caracteristica caracteristica;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
        caracteristica = new Caracteristica();
        caracteristica.setId(1L);
        caracteristica.setDescripcion("camaras");
        caracteristica.setProductos(new ArrayList<Producto>());

    }

    @Test
    void crearCaracteristica() {
        when(caracteristicaRepository.save(caracteristica)).thenReturn(caracteristica);

        caracteristicaServiceImpl.CrearCaracteristica(caracteristica);

        Mockito.verify(caracteristicaRepository, Mockito.times(1)).save(caracteristica);

    }

    @Test
    void buscarCaracteristica() {

        when(caracteristicaRepository.findById(1L)).thenReturn(Optional.of(caracteristica));
        Optional<Caracteristica> result = caracteristicaServiceImpl.BuscarCaracteristica(1L);
        assertTrue(result.isPresent(),"caracteristica encontrada");
    }

    @Test
    void buscarCaracteristicaError() {

        when(caracteristicaRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<Caracteristica> result = caracteristicaServiceImpl.BuscarCaracteristica(1L);
        assertFalse(result.isPresent(), "caracteristica no encontrada");
    }

    @Test
    void traerTodos() {
        when(caracteristicaRepository.findAll()).thenReturn(List.of(caracteristica));

        List<Caracteristica>caracteristicas= caracteristicaServiceImpl.TraerTodos();
        assertEquals(List.of(caracteristica),caracteristicas);
    }

    @Test
    void modificarCaracteristica() {
        when(caracteristicaRepository.save(caracteristica));

        caracteristicaServiceImpl.ModificarCaracteristica(caracteristica);

        Mockito.verify(caracteristicaRepository, Mockito.times(1)).save(caracteristica);
    }

    @Test
    void eliminarCaracteristica() {

        when(caracteristicaRepository.findById(caracteristica.getId())).thenReturn(Optional.of(caracteristica));

         caracteristicaServiceImpl.EliminarCaracteristica( caracteristica.getId());

        verify(caracteristicaRepository, times(1)).deleteById(caracteristica.getId());

    }

    @Test
    void eliminarCaracteristica_CaracteristicaNoEncontrada() {

        when(caracteristicaRepository.findById(caracteristica.getId())).thenReturn(Optional.empty());

        CaracteristicaNotFoundException exception = assertThrows(
                CaracteristicaNotFoundException.class,
                () -> caracteristicaServiceImpl.EliminarCaracteristica(caracteristica.getId())
        );

        assertEquals("No se encontró una característica con id: " + caracteristica.getId(), exception.getMessage());
        verify(caracteristicaRepository, never()).deleteById(caracteristica.getId());
    }
}