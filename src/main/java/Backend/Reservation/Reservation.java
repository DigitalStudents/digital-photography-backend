package Backend.Reservation;

import Backend.Producto.Producto;
import Backend.User.Model.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Producto producto;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private UserEntity user;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "America/Argentina/Buenos_Aires")
    @Column(name = "start_date")
    private LocalDateTime startDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "America/Argentina/Buenos_Aires")
    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "total_price")
    private double totalPrice;


    public double calculateTotalPrice() {
        long diffInDays = ChronoUnit.DAYS.between(startDate, endDate);
        double precio = producto.getPrecio();

        return diffInDays * precio;
    }
}