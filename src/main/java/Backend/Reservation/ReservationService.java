package Backend.Reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface ReservationService {
    void createReservation(Reservation reservation);
    void updateReservation(Long id, Reservation reservation);
    void deleteReservation(Long id);
    Reservation getReservation(Long id);
    List<ReservationDTO> getAllReservationsDTO();
    List<Reservation> getOverlappingReservations(Long productId, LocalDateTime startDate, LocalDateTime endDate);
    List<ReservationDTO> getAllReservationsForUser(Long userId);

    List<ReservationDTO> getAllReservationsForProduct(Long productId);
    boolean findByUser_IdAndProducto_Id(Long userId, Long productId);

}