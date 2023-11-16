package Backend.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailTestController {

    @Autowired
    private EmailTestService emailTestService;

    @PostMapping("/send-test-email")
    public ResponseEntity<String> sendTestEmail(@RequestParam String to) {
        try {
            emailTestService.sendTestEmail(to);
            return new ResponseEntity<>("Email Test enviado con éxito.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al enviar el Email. Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
