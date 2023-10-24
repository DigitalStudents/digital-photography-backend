package Backend.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoService {

     void CrearProducto (Producto producto);
     Optional<Producto> BuscarProducto(Long id);
     List<Producto> TraerTodos();
     void ModificarProducto (Producto producto);
     void EliminarProducto (Long id);
     void RestaurarProducto(Long id);

     List<Producto> obtenerProductosAleatorios(int cantidad);
}
