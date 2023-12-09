package Backend.Producto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto=new Producto();
    }

    @Test
    void createProducto() {
    }

    @Test
    void uploadImage() {
    }

    @Test
    void getProducto() {
    }

    @Test
    void getAllProductos() {
    }

    @Test
    void getPagination() {
    }

    @Test
    void searchProductos() {
    }

    @Test
    void filterProductosByCategorias() {
    }

    @Test
    void updateProducto() {
    }

    @Test
    void addCaracteristicasToProducto() {
    }

    @Test
    void addCategoriasToProducto() {
    }

    @Test
    void deleteProducto() {
    }

    @Test
    void addRating() {
    }

    @Test
    void getProductRatings() {
    }
}