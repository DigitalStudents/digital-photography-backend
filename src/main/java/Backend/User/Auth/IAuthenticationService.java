package Backend.User.Auth;

import Backend.User.dto.UserEntityDTO;

public interface IAuthenticationService{

    String login (AuthenticationRequest authenticationRequest);
}
