package Backend.User.Auth;
import Backend.User.dto.UserEntityDTO;
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

    @GetMapping("/hola")
    public ResponseEntity<String> hola (){
        return new ResponseEntity<>("hola spring", HttpStatus.OK);
    }

}
