package Backend.Reservation;

import java.util.Date;
import java.util.List;

public interface ReservationService {
    void createReservation(Reservation reservation);
    void updateReservation(Long id, Reservation reservation);
    void deleteReservation(Long id);
    Reservation getReservation(Long id);
    List<ReservationDTO> getAllReservationsDTO();
    List<Reservation> getOverlappingReservations(Long productId, Date startDate, Date endDate);
    List<ReservationDTO> getAllReservationsForUser(Long userId);

    List<ReservationDTO> getAllReservationsForProduct(Long productId);
}