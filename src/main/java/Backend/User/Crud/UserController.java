package Backend.User.Crud;
import Backend.User.dto.RoleUpdate;
import Backend.User.dto.UserEntityDTO;
import Backend.User.dto.UserIdentityDTO;
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

    @PostMapping("/register")
    public ResponseEntity<String>register(@Valid @RequestBody UserEntityDTO userEntityDTO){

        return new ResponseEntity<>(iUserService.register(userEntityDTO),HttpStatus.OK);
    }

    @PutMapping("/roleUpdate")
    public ResponseEntity<?> roleUpdate(@Valid @RequestBody List<RoleUpdate> roleUpdate) {
        return new ResponseEntity<>(iUserService.roleUpdate(roleUpdate), HttpStatus.OK);
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(@Valid @RequestParam Long id){
        return new ResponseEntity<>(iUserService.deleteById(id),HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserIdentityDTO>> users(){
        return new ResponseEntity<>(iUserService.findAll(),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserEntityDTO> users(@RequestParam Long id){
        return new ResponseEntity<>(iUserService.findByid(id),HttpStatus.OK);
    }
}
