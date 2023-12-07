package Backend.Reservation;

import lombok.Data;

@Data
public class CreateReservationRequestDTO {
    private Long productId;
    private String startDate;
    private String endDate;
    private Integer startHour;
    private Integer endHour;
}
