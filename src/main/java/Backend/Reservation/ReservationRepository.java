package Backend.Reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r " +
            "WHERE r.producto.id = :productId " +
            "AND ((:startDate BETWEEN r.startDate AND r.endDate) " +
            "OR (:endDate BETWEEN r.startDate AND r.endDate))")
    List<Reservation> findOverlappingReservations(@Param("productId") Long productId,
                                                  @Param("startDate") LocalDateTime startDate,
                                                  @Param("endDate") LocalDateTime endDate);

    List<Reservation> findByUser_Id(Long userId);
    List<Reservation> findByProducto_Id(Long productId);
    List<Reservation> findByUser_IdAndProducto_Id(Long userId, Long productId);
    List<Reservation> findByUser_IdOrderByStartDate(Long userId);

}