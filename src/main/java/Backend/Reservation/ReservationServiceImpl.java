package Backend.Reservation;

import Backend.Email.EmailService;
import Backend.Producto.Producto;
import Backend.Producto.ProductoRepository;
import Backend.User.Crud.UserRepository;
import Backend.exceptions.ProductNotFoundException;
import Backend.exceptions.ReservationNotFoundException;
import Backend.exceptions.UserNotFoundException;
import jakarta.mail.MessagingException;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public void createReservation(Reservation reservation) {
        if (hasOverlappingReservations(reservation)) {
            throw new RuntimeException("El producto ya está reservado para la fecha indicada.");
        }
        reservationRepository.save(reservation);
        sendReservationConfirmationEmail(reservation);
    }

    private void sendReservationConfirmationEmail(Reservation reservation) {
        String userEmail = reservation.getUser().getUsername();
        String productName = reservation.getProducto().getNombre();
        LocalDateTime startDate = reservation.getStartDate();
        LocalDateTime endDate = reservation.getEndDate();
        double totalPrice = reservation.getTotalPrice();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE yyyy-MM-dd HH", new Locale("es", "ES"));
        String formattedStartDate = startDate.format(formatter);
        String formattedEndDate = endDate.format(formatter);

        try {
            emailService.sendReservationConfirmationEmail(userEmail, productName, formattedStartDate, formattedEndDate, totalPrice);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateReservation(Long id, Reservation reservation) {
        if (hasOverlappingReservations(reservation)) {
            throw new RuntimeException("Ya hay una reserva del producto dentro de las fechas indicadas");
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
                .sorted(Comparator.comparing(Reservation::getStartDate))
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

    @Override
    public boolean findByUser_IdAndProducto_Id(Long userId, Long productId) {
        List<Reservation> reservations = reservationRepository.findByUser_IdAndProducto_Id(userId, productId);
        return !reservations.isEmpty();
    }

    private ReservationDTO convertEntityToDTOWithProductDetails(Reservation reservation) {
        Producto product = productoRepository.findById(reservation.getProducto().getId()).orElse(null);
        if (product == null) {
            throw new RuntimeException("Producto no encontrado para la reserva con id: " + reservation.getId());
        }

        return new ReservationDTO(
                reservation.getId(),
                reservation.getProducto().getId(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getProducto().getNombre(),
                reservation.getUser().getId(),
                reservation.getTotalPrice()
        );
    }

    private ReservationDTO convertEntityToDTO(Reservation reservation) {
        return new ReservationDTO(
                reservation.getId(),
                reservation.getProducto().getId(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getProducto().getNombre(),
                reservation.getUser().getId(),
                reservation.getTotalPrice()
        );
    }

    @Override
    public List<Reservation> getOverlappingReservations(Long productId, LocalDateTime startDate, LocalDateTime endDate) {
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