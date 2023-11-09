package Backend.Security;

import Backend.User.UserDTO;
import jakarta.mail.MessagingException;

public interface IAuthenticationService{

    String login (AuthenticationRequest authenticationRequest);
    String register (UserDTO userDTO) throws MessagingException;
}
