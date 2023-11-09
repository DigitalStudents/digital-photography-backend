package Backend.Security;
import Backend.User.UserDTO;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user/auth")
public class AuthenticationController {

    @Autowired
    IAuthenticationService iAuthenticationService ;

    @PostMapping("/login")
    public ResponseEntity<String>Login(@Valid @RequestBody AuthenticationRequest authenticationRequest){

        return new ResponseEntity<>(iAuthenticationService.login(authenticationRequest),HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String>register(@Valid @RequestBody UserDTO userDTO) throws MessagingException {

        return new ResponseEntity<>(iAuthenticationService.register(userDTO),HttpStatus.OK);
    }


}
