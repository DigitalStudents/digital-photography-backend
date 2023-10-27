package Backend;



import Backend.Producto.Producto;
import Backend.Producto.ProductoRepository;
import Backend.Producto.ProductoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoServiceImpl ProductoService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProducto() {
        Producto producto = new Producto();

        when(productoRepository.save(producto)).thenReturn(producto);

        ProductoService.CrearProducto(producto);

        Mockito.verify(productoRepository, Mockito.times(1)).save(producto);
    }

    @Test
    public void testBuscarProducto() {
        Long id = 1L;
        Producto producto = new Producto();
        when(productoRepository.findById(id)).thenReturn(Optional.of(producto));

        Optional<Producto> result = ProductoService.BuscarProducto(id);
        assertTrue(result.isPresent());
        assertEquals(producto, result.get());
    }

    @Test
    public void testTraerTodos() {
        List<Producto> producto = new ArrayList<>();
        when(productoRepository.findAll()).thenReturn(producto);

        List<Producto> result = ProductoService.TraerTodos();
        assertEquals(producto, result);
    }

    @Test
    public void testModificarProducto() {
        Producto producto = new Producto();
        when(productoRepository.save(producto)).thenReturn(producto);

        ProductoService.ModificarProducto(producto);
        Mockito.verify(productoRepository, Mockito.times(1)).save(producto);
    }

    @Test
    public void testEliminarproducto() {
        Long id = 1L;
        ProductoService.EliminarProducto(id);
        Mockito.verify(productoRepository, Mockito.times(1)).deleteById(id);
    }
}
