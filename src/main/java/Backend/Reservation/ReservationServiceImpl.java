package Backend.Reservation;

import Backend.Producto.Producto;
import Backend.Producto.ProductoRepository;
import Backend.User.Crud.UserRepository;
import Backend.exceptions.ProductNotFoundException;
import Backend.exceptions.ReservationNotFoundException;
import Backend.exceptions.UserNotFoundException;
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

    @Autowired
    private UserRepository userRepository;

    @Override
    public void createReservation(Reservation reservation) {
        if (hasOverlappingReservations(reservation)) {
            throw new RuntimeException("El producto ya está reservado para la fecha indicada.");
        }
        reservationRepository.save(reservation);
    }

    @Override
    public void updateReservation(Long id, Reservation reservation) {
        if (hasOverlappingReservations(reservation)) {
            throw new RuntimeException("El producto ya está reservado para la fecha indicada");
        }
        reservation.setId(id);
        reservationRepository.save(reservation);
    }

    @Override
    public void deleteReservation(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new ReservationNotFoundException("Reserva no encontrada con ID: " + id);
        }
        reservationRepository.deleteById(id);
    }
    @Override
    public Reservation getReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("Reserva no encontrada con id: " + id));
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
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Usuario no encontrado con id: " + userId);
        }
        List<Reservation> reservations = reservationRepository.findByUser_Id(userId);
        return reservations.stream()
                .map(this::convertEntityToDTOWithProductDetails)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReservationDTO> getAllReservationsForProduct(Long productId) {
        if (!productoRepository.existsById(productId)) {
            throw new ProductNotFoundException("Producto no encontrado con id: " + productId);
        }
        List<Reservation> reservations = reservationRepository.findByProducto_Id(productId);
        return reservations.stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    private ReservationDTO convertEntityToDTOWithProductDetails(Reservation reservation) {
        Producto product = productoRepository.findById(reservation.getProducto().getId()).orElse(null);
        if (product == null) {
            throw new RuntimeException("Producto no encontrado para la reserva con id: " + reservation.getId());
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