package Backend.Reservation;

import Backend.Producto.Producto;
import Backend.User.Model.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private UserEntity user;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_date")
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "total_price")
    private double totalPrice;


    public double calculateTotalPrice() {
        long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
        long diffInDays = diffInMillies / (24 * 60 * 60 * 1000);

        double precio = producto.getPrecio(); // Debugging statement

        double calculatedTotalPrice = diffInDays * precio;

        System.out.println("Debug: Precio: " + precio);
        System.out.println("Debug: Diff in Days: " + diffInDays);
        System.out.println("Debug: Calculated Total Price: " + calculatedTotalPrice);

        return calculatedTotalPrice;
    }
}