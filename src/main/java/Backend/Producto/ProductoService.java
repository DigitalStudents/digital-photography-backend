package Backend.Producto;

import Backend.ProductRating.ProductRating;
import Backend.ProductRating.ProductRatingDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ProductoService {

     void CrearProducto (Producto producto);
     void uploadImages(Long productId, List<MultipartFile> imageFiles) throws IOException;
     Optional<Producto> BuscarProducto(Long id);
     List<Producto> TraerTodos();

     void ModificarProducto (Producto producto);
     void EliminarProducto (Long id);
     void RestaurarProducto(Long id);
     Page<Producto> Paginados(Pageable pageable);

     Page<Producto> BuscarPorNombre(String searchTerm, Pageable pageable);

     List<Producto> filterProductosByCategorias(List<String> categoriaNombres);

     void agregarCaracteristicasAProducto(Long productoId, List<Long> caracteristicaIds);

     void agregarCategoriasAProducto(Long productoId, List<Long> categoriaIds);

     void addRating(Long productId, Long userId, int rating, String comment);
     double getAverageRating(Long productId);
     List<ProductRatingDTO> getProductRatings(Long productId);


}
