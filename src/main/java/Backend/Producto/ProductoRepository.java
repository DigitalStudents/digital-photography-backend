package Backend.Producto;

import Backend.Reservation.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    Page<Producto> findByNombreContainingIgnoreCase(String searchTerm, Pageable pageable);

    List<Producto> findByCategorias_NombreIn(List<String> categoriaNombres);

    Optional<Producto> findByNombreIgnoreCase(String nombre);

}
