package Backend.Reservation;

import Backend.Producto.Producto;
import Backend.Producto.ProductoRepository;
import Backend.Security.JwtUtils;
import Backend.User.Crud.UserRepository;
import Backend.User.Model.UserEntity;
import Backend.exceptions.ProductNotFoundException;
import Backend.exceptions.ReservationNotFoundException;
import Backend.exceptions.UserNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.jsonwebtoken.Claims;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/v1/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);


    @PostMapping
    @Operation(summary = "Crea una Reserva")
    public ResponseEntity<String> createReservation(
            @RequestBody ReservationRequest reservationRequest,
            HttpServletRequest request) {

        try {
            String username = getUsernameFromToken(request.getHeader("Authorization"));

            Producto producto = productoRepository.findById(reservationRequest.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado con id: " + reservationRequest.getProductId()));

            UserEntity userEntity = userRepository.findByUsername(username)
                    .orElseGet(() -> {

                        UserEntity newUser = new UserEntity();
                        newUser.setUsername(username);
                        userRepository.save(newUser);
                        return newUser;
                    });

            Date currentDate = new Date();
            Date startDate = parseStringToDate(reservationRequest.getStartDate());
            if (startDate.before(currentDate)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se puede reservar para fechas pasadas.");
            }


            Reservation reservation = new Reservation();
            reservation.setStartDate(parseStringToDate(reservationRequest.getStartDate()));
            reservation.setEndDate(parseStringToDate(reservationRequest.getEndDate()));
            reservation.setProducto(producto);
            reservation.setUser(userEntity);

            double totalPrice = reservation.calculateTotalPrice();
            reservation.setTotalPrice(totalPrice);

            reservationService.createReservation(reservation);

            return ResponseEntity.ok().build();
        } catch (ProductNotFoundException | UserNotFoundException | ReservationNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (RuntimeException e) {
            logger.error("An unexpected error occurred: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ha ocurrido un error al crearse la reserva: " + e.getMessage());
        }
    }


    private Date parseStringToDate(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Error parsing date string: " + dateString, e);
        }
    }

    private String getUsernameFromToken(String tokenHeader) {
        try {
            if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
                String token = tokenHeader.substring(7);

                if (jwtUtils.isTokenValid(token)) {
                    return jwtUtils.getClaim(token, Claims::getSubject);
                }
            }
            throw new RuntimeException("JWT token no existente o inválido");
        } catch (Exception e) {
            throw new RuntimeException("Error extrayendo el UserName del JWT: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Trae una reservar por su Id")
    public ResponseEntity<?> getReservation(@PathVariable Long id) {
        try {
            Reservation reservation = reservationService.getReservation(id);
            if (reservation == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron reservas con id: " + id);
            }
            return ResponseEntity.ok(reservation);
        } catch (ReservationNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error trayendo reserva con id: " + id);
        }
    }

    @GetMapping
    @Operation(summary = "Trae todas las Reservas")
    public List<ReservationDTO> getAllReservationsDTO() {
        return reservationService.getAllReservationsDTO();
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Trae todas las reservas de un producto")
    public ResponseEntity<?> getAllReservationsForProduct(@PathVariable Long productId) {
        try {
            List<ReservationDTO> reservations = reservationService.getAllReservationsForProduct(productId);
            if (reservations.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El producto con id: " + productId + " No tiene reservas");
            }
            return ResponseEntity.ok(reservations);
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Borra una reserva")
    public void deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Trae todas las reservas de un usuario")
    public ResponseEntity<?> getAllReservationsForUser(@PathVariable Long userId) {
        try {
            List<ReservationDTO> reservations = reservationService.getAllReservationsForUser(userId);
            if (reservations.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El usuario con ID: " + userId + " no tiene reservas" );
            }
            return ResponseEntity.ok(reservations);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}