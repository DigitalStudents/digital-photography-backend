package Backend.Producto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductoService {

     void CrearProducto (Producto producto);
     Optional<Producto> BuscarProducto(Long id);
     List<Producto> TraerTodos();
     void ModificarProducto (Producto producto);
     void EliminarProducto (Long id);
     void RestaurarProducto(Long id);
     Page<Producto> Paginados(Pageable pageable);

     void subirImagen(String imageUrl, byte[] imageBytes);


}
