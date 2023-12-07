package Backend.Reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDTO {

    private Long id;
    private Long productId;
    @JsonFormat(pattern = "yyyy-MM-dd HH")
    private LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH")
    private LocalDateTime endDate;
    private String productName;
    private Long userId;
    private double totalPrice;

}