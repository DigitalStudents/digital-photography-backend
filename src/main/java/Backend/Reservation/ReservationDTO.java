package Backend.Reservation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDTO {

    private Long id;
    private Date startDate;
    private Date endDate;
    private Long productId;
    private String productName;
    private Long userId;

}