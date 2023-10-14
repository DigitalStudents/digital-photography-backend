package PICamada4Equipo4.demo;


import PICamada4Equipo4.demo.CamaraDeFotos.CamaraDeFotos;
import PICamada4Equipo4.demo.CamaraDeFotos.CamaraDeFotosRepository;
import PICamada4Equipo4.demo.CamaraDeFotos.CamaraDeFotosServiceImpl;
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
public class CamaraDeFotosServiceTest {

    @Mock
    private CamaraDeFotosRepository camaraDeFotosRepository;

    @InjectMocks
    private CamaraDeFotosServiceImpl camaraDeFotosService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCrearCamaraDeFotos() {
        CamaraDeFotos camara = new CamaraDeFotos();

        when(camaraDeFotosRepository.save(camara)).thenReturn(camara);

        camaraDeFotosService.CrearCamaraDeFotos(camara);

        Mockito.verify(camaraDeFotosRepository, Mockito.times(1)).save(camara);
    }

    @Test
    public void testBuscarCamaraDeFotos() {
        Long id = 1L;
        CamaraDeFotos camara = new CamaraDeFotos();
        when(camaraDeFotosRepository.findById(id)).thenReturn(Optional.of(camara));

        Optional<CamaraDeFotos> result = camaraDeFotosService.BuscarCamaraDeFotos(id);
        assertTrue(result.isPresent());
        assertEquals(camara, result.get());
    }

    @Test
    public void testTraerTodos() {
        List<CamaraDeFotos> camaras = new ArrayList<>();
        when(camaraDeFotosRepository.findAll()).thenReturn(camaras);

        List<CamaraDeFotos> result = camaraDeFotosService.TraerTodos();
        assertEquals(camaras, result);
    }

    @Test
    public void testModificarCamaraDeFotos() {
        CamaraDeFotos camara = new CamaraDeFotos();
        when(camaraDeFotosRepository.save(camara)).thenReturn(camara);

        camaraDeFotosService.ModificarCamaraDeFotos(camara);
        Mockito.verify(camaraDeFotosRepository, Mockito.times(1)).save(camara);
    }

    @Test
    public void testEliminarCamaraDeFotos() {
        Long id = 1L;
        camaraDeFotosService.EliminarCamaraDeFotos(id);
        Mockito.verify(camaraDeFotosRepository, Mockito.times(1)).deleteById(id);
    }
}
