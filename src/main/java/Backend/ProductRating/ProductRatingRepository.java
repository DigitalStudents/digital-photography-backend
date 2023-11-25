package Backend.ProductRating;

import Backend.Producto.Producto;
import Backend.User.Model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRatingRepository extends JpaRepository<ProductRating, Long> {
    List<ProductRating> findByProduct_Id(Long productId);
    boolean existsByProductAndUser(Producto product, UserEntity user);
}