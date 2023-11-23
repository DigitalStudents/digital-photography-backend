package Backend.Reservation;

import Backend.Producto.Producto;
import Backend.Producto.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public void createReservation(Reservation reservation) {
        if (hasOverlappingReservations(reservation)) {
            throw new RuntimeException("Overlapping reservations are not allowed.");
        }
        reservationRepository.save(reservation);
    }

    @Override
    public void updateReservation(Long id, Reservation reservation) {
        if (hasOverlappingReservations(reservation)) {
            throw new RuntimeException("Overlapping reservations are not allowed.");
        }
        reservation.setId(id);
        reservationRepository.save(reservation);
    }

    @Override
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    @Override
    public Reservation getReservation(Long id) {
        return reservationRepository.findById(id).orElse(null);
    }

    @Override
    public List<ReservationDTO> getAllReservationsDTO() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReservationDTO> getAllReservationsForUser(Long userId) {
        List<Reservation> reservations = reservationRepository.findByUser_Id(userId);
        return reservations.stream()
                .map(this::convertEntityToDTOWithProductDetails)
                .collect(Collectors.toList());
    }

    private ReservationDTO convertEntityToDTOWithProductDetails(Reservation reservation) {
        Producto product = productoRepository.findById(reservation.getProducto().getId()).orElse(null);
        if (product == null) {
            throw new RuntimeException("Product not found for reservation id: " + reservation.getId());
        }

        return new ReservationDTO(
                reservation.getId(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getProducto().getId(),
                reservation.getProducto().getNombre(),
                reservation.getUser().getId(),
                reservation.getTotalPrice()
        );
    }

    private ReservationDTO convertEntityToDTO(Reservation reservation) {
        return new ReservationDTO(
                reservation.getId(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getProducto().getId(),
                reservation.getProducto().getNombre(),
                reservation.getUser().getId(),
                reservation.getTotalPrice()
        );
    }

    @Override
    public List<Reservation> getOverlappingReservations(Long productId, Date startDate, Date endDate) {
        return reservationRepository.findOverlappingReservations(productId, startDate, endDate);
    }

    private boolean hasOverlappingReservations(Reservation reservation) {
        List<Reservation> overlappingReservations = getOverlappingReservations(
                reservation.getProducto().getId(),
                reservation.getStartDate(),
                reservation.getEndDate()
        );
        return !overlappingReservations.isEmpty();
    }
}