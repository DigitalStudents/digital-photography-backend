package Backend.Reservation;

import Backend.Producto.Producto;
import Backend.Producto.ProductoRepository;
import Backend.User.Model.UserEntity;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/v1/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ProductoRepository productoRepository;

    @PostMapping
    @Operation(summary = "Crea una Reserva")
    public void createReservation(
            @RequestParam(name = "productId") Long productId,
            @RequestParam(name = "userId") Long userId,
            @RequestParam(name = "startDate") @DateTimeFormat(pattern = "mm-dd-yyyy") Date startDate,
            @RequestParam(name = "endDate") @DateTimeFormat(pattern = "mm-dd-yyyy") Date endDate) {

        Producto producto = productoRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto not found with id: " + productId));

        Reservation reservation = new Reservation();
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);
        reservation.setProducto(producto);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        reservation.setUser(userEntity);

        double totalPrice = reservation.calculateTotalPrice();
        reservation.setTotalPrice(totalPrice);

        reservationService.createReservation(reservation);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Trae una reservar por su Id")
    public Reservation getReservation(@PathVariable Long id) {
        return reservationService.getReservation(id);
    }

    @GetMapping
    @Operation(summary = "Trae todas las Reservas")
    public List<ReservationDTO> getAllReservationsDTO() {
        return reservationService.getAllReservationsDTO();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Borra una reserva")
    public void deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
    }
    @GetMapping("/user/{userId}")
    @Operation(summary = "Trae todas las reservas de un usuario")
    public List<ReservationDTO> getAllReservationsForUser(@PathVariable Long userId) {
        return reservationService.getAllReservationsForUser(userId);
    }
}