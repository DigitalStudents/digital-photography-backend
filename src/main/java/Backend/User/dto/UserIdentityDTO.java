package Backend.User.dto;

import Backend.User.Model.ERole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserIdentityDTO {

        private Long id;
        private String firstName;
        private String lastName;
        private String username;

        @Enumerated(EnumType.STRING)
        private ERole role;

}
