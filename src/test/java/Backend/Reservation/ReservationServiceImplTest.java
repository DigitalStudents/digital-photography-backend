package Backend.Reservation;

import Backend.Email.EmailService;
import Backend.Producto.Producto;
import Backend.Producto.ProductoRepository;
import Backend.User.Crud.UserRepository;
import Backend.User.Model.UserEntity;
import Backend.exceptions.ReservationNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private ReservationServiceImpl reservationServiceImpl;

    private Reservation reservation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        reservation = new Reservation();
        reservation.setStartDate(new Date(1641782400000L));
        reservation.setEndDate(new Date(1641868800000L));
        reservation.setProducto(new Producto());
        reservation.setUser(new UserEntity());
        reservation.setId(1L);
    }

    @Test
    void createReservation() {

        Long id= reservation.getProducto().getId();
        Date startDate=reservation.getStartDate();
        Date endDate=reservation.getEndDate();

        when(reservationRepository.findOverlappingReservations(id,startDate,endDate)).thenReturn(Collections.emptyList());

        assertDoesNotThrow(() -> reservationServiceImpl.createReservation(reservation));

        verify(reservationRepository, times(1)).save(reservation);

        assertDoesNotThrow(() -> {
            Mockito.verify(emailService, Mockito.never()).sendReservationConfirmationEmail(
                    Mockito.anyString(),
                    Mockito.anyString(),
                    Mockito.anyString(),
                    Mockito.anyString(),
                    Mockito.anyDouble()
            );
        });

    }

    @Test
    void createReservationError() {
        Long id= reservation.getProducto().getId();
        Date startDate=reservation.getStartDate();
        Date endDate=reservation.getEndDate();

        when(reservationRepository.findOverlappingReservations(id,startDate,endDate)).thenReturn(List.of(reservation));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reservationServiceImpl.createReservation(reservation);
        });

        assertEquals("El producto ya está reservado para la fecha indicada.", exception.getMessage());

        verify(reservationRepository, never()).save(reservation);

        assertDoesNotThrow(() -> {
            Mockito.verify(emailService, Mockito.never()).sendReservationConfirmationEmail(
                    Mockito.anyString(),
                    Mockito.anyString(),
                    Mockito.anyString(),
                    Mockito.anyString(),
                    Mockito.anyDouble()
            );
        });

    }


//    @Test
//    void updateReservation() {
//        Long id= reservation.getProducto().getId();
//        Date startDate=reservation.getStartDate();
//        Date endDate=reservation.getEndDate();
//
//        when(reservationRepository.findOverlappingReservations(id,startDate,endDate)).thenReturn(null);
//
//        reservationServiceImpl.updateReservation(1L,reservation);
//
//        verify(reservationRepository, times(1)).save(reservation);
//
//    }

    @Test
    void updateReservationError() {
        Long id= reservation.getProducto().getId();
        Date startDate=reservation.getStartDate();
        Date endDate=reservation.getEndDate();

        when(reservationRepository.findOverlappingReservations(id,startDate,endDate)).thenReturn(List.of(reservation));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reservationServiceImpl.createReservation(reservation);
        });

        assertEquals("El producto ya está reservado para la fecha indicada.", exception.getMessage());

        verify(reservationRepository, never()).save(reservation);

    }

    @Test
    void deleteReservation() {
        when(reservationRepository.existsById(1L)).thenReturn(true);

        reservationServiceImpl.deleteReservation(1L);

        verify(reservationRepository, times(1)).deleteById(eq(1L));
    }

    void deleteReservationError() {

        when(reservationRepository.existsById(1L)).thenReturn(false);


        assertThrows(ReservationNotFoundException.class, () -> reservationServiceImpl.deleteReservation(1L));


        verify(reservationRepository, never()).deleteById(eq(1L));

    }

    @Test
    void getReservation() {
        when(reservationRepository.findById(1L)).thenReturn(java.util.Optional.of(reservation));

        Reservation result = reservationServiceImpl.getReservation(1L);

        assertEquals(reservation, result);
    }

    @Test
    void getReservationError() {
        when(reservationRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(ReservationNotFoundException.class, () -> reservationServiceImpl.getReservation(1L));

        verify(reservationRepository, times(1)).findById(eq(1L));
    }


    @Test
    void getAllReservationsDTO() {

    }

    @Test
    void getAllReservationsForUser() {
    }

    @Test
    void getAllReservationsForProduct() {
    }

    @Test
    void findByUser_IdAndProducto_Id() {
        List<Reservation> reservations = Arrays.asList(reservation);
        when(reservationRepository.findByUser_IdAndProducto_Id(1L, 2L)).thenReturn(reservations);

        boolean result = reservationServiceImpl.findByUser_IdAndProducto_Id(1L, 2L);

        assertTrue(result);

        verify(reservationRepository, times(1)).findByUser_IdAndProducto_Id(eq(1L), eq(2L));
    }

    @Test
    void getOverlappingReservations() {
    }
}