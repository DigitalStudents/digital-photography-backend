package Backend.User.Crud;
import Backend.Producto.Producto;
import Backend.User.dto.RoleUpdate;
import Backend.User.dto.UserEntityDTO;
import Backend.User.dto.UserIdentityDTO;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("user/crud")
public class UserController {

    @Autowired
    private IUserService iUserService;

    @PostMapping("/register")
    public ResponseEntity<String>register(@Valid @RequestBody UserEntityDTO userEntityDTO) throws MessagingException {

        return new ResponseEntity<>(iUserService.register(userEntityDTO),HttpStatus.OK);
    }

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserEntityDTO userEntityDTO) throws MessagingException {
       return new ResponseEntity<>(iUserService.create(userEntityDTO), HttpStatus.OK);
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

    @PostMapping("/{userId}/agregarFavorito/{productId}")
    public ResponseEntity<?> addToFavorites(@PathVariable Long userId, @PathVariable Long productId) {
        iUserService.addToFavorites(userId, productId);
        return new ResponseEntity<>("Producto" + productId +" agregado a favoritos", HttpStatus.OK);
    }

    @GetMapping("/{userId}/productosFavoritos")
    public ResponseEntity<List<Producto>> getFavoriteProducts(@PathVariable Long userId) {
        List<Producto> favoriteProducts = iUserService.getFavoriteProducts(userId);
        return new ResponseEntity<>(favoriteProducts, HttpStatus.OK);
    }

    @PostMapping("/removerFavorito")
    public ResponseEntity<?> removeFavorite(@RequestParam Long userId, @RequestParam Long productId) {
        iUserService.removeFavoriteProduct(userId, productId);
        return new ResponseEntity<>("Producto" + productId + " removido de favoritos", HttpStatus.OK);
    }
}
