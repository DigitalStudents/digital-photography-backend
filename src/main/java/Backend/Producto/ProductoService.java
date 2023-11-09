package Backend.Producto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ProductoService {

     void CrearProducto (Producto producto);
     void uploadImage(Long productId, MultipartFile image) throws IOException;
     Optional<Producto> BuscarProducto(Long id);
     List<Producto> TraerTodos();

     void ModificarProducto (Producto producto);
     void EliminarProducto (Long id);
     void RestaurarProducto(Long id);
     Page<Producto> Paginados(Pageable pageable);

     Page<Producto> BuscarPorNombre(String searchTerm, Pageable pageable);

}
