package Backend.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user/crud")
public class UserController {

    @Autowired
    private IUserService iUserService;

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO) {
       return new ResponseEntity<>(iUserService.save(userDTO), HttpStatus.OK);
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(@Valid @RequestParam Long id){
        return new ResponseEntity<>(iUserService.deleteById(id),HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> users(){
        return new ResponseEntity<>(iUserService.findAll(),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> users(@RequestParam Long id){
        return new ResponseEntity<>(iUserService.findByid(id),HttpStatus.OK);
    }
}
