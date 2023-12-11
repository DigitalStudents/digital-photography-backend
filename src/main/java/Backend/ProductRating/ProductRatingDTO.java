package Backend.ProductRating;

import Backend.User.Model.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRatingDTO {

    private Long id;
    private int rating;
    private String comment;
    private String date;
    private String userName;

    public ProductRatingDTO(ProductRating productRating) {
        this.id = productRating.getId();
        this.rating = productRating.getRating();
        this.comment = productRating.getComment();
        this.date = productRating.getDate().toLocalDate().toString();
        UserEntity user = productRating.getUser();
        if (user != null) {
            this.userName = user.getFirstName() + " " + user.getLastName();
        }
    }
}