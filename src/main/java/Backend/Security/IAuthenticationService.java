package Backend.Security;

import Backend.User.UserDTO;

public interface IAuthenticationService{

    String login (AuthenticationRequest authenticationRequest);
    String register (UserDTO userDTO);
}
