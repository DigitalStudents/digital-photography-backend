package Backend.User.Auth;
import Backend.User.Crud.UserService;
import Backend.User.dto.UserEntityDTO;
import io.swagger.v3.oas.annotations.Operation;
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

    @Autowired
    UserService userService ;

    @PostMapping("/login")
    public ResponseEntity<String>Login(@Valid @RequestBody AuthenticationRequest authenticationRequest){

        return new ResponseEntity<>(iAuthenticationService.login(authenticationRequest),HttpStatus.OK);
    }

<<<<<<< HEAD
    @GetMapping("/hola")
    public ResponseEntity<String> hola (){
        return new ResponseEntity<>("hola spring", HttpStatus.OK);
    }
=======
    @GetMapping("/verify")
    @Operation(summary = "Valida la cuenta usando el token generado en registro")
    public ResponseEntity<String> verifyAccount(@RequestParam("token") String verificationToken) {
        boolean isTokenValid = userService.verifyAccount(verificationToken);

        if (isTokenValid) {
            return new ResponseEntity<>("Cuenta verificada correctamente. Ya puede ingresar.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Token de verificación inválido. Por favor revise su email.", HttpStatus.BAD_REQUEST);
        }
    }

>>>>>>> 1c438e51880c14d65b231a03c7a6ad6380760560

}
