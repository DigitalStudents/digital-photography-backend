package Backend.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/verification")
public class VerificationController {

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/verify")
    public ResponseEntity<String> verifyAccount(@RequestParam("token") String token) {
        boolean isVerified = authenticationService.verifyAccount(token);

        if (isVerified) {
            return ResponseEntity.ok("Cuenta verificada. Ya puedes logear.");
        } else {
            return ResponseEntity.badRequest().body("Token inválido o expirado.");
        }
    }
}