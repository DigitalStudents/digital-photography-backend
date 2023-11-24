package Backend.Reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r " +
            "WHERE r.producto.id = :productId " +
            "AND ((:startDate BETWEEN r.startDate AND r.endDate) " +
            "OR (:endDate BETWEEN r.startDate AND r.endDate))")
    List<Reservation> findOverlappingReservations(@Param("productId") Long productId,
                                                  @Param("startDate") Date startDate,
                                                  @Param("endDate") Date endDate);

    List<Reservation> findByUser_Id(Long userId);
    List<Reservation> findByProducto_Id(Long productId);
}