package Backend.User.Crud;
import Backend.Producto.Producto;
import Backend.Security.JwtUtils;
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

    @Autowired
    private JwtUtils jwtUtils;

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

    @PostMapping("/agregarFavorito/{productId}")
    public ResponseEntity<?> addToFavorites(
            @PathVariable Long productId,
            @RequestHeader("Authorization") String tokenHeader) {
        Long userId = getUserIdFromToken(tokenHeader);
        iUserService.addToFavorites(userId, productId);
        return new ResponseEntity<>("Producto " + productId + " agregado a favoritos", HttpStatus.OK);
    }

    @GetMapping("/{userId}/productosFavoritos")
    public ResponseEntity<List<Producto>> getFavoriteProducts(@PathVariable Long userId) {
        List<Producto> favoriteProducts = iUserService.getFavoriteProducts(userId);
        return new ResponseEntity<>(favoriteProducts, HttpStatus.OK);
    }

    @PostMapping("/removerFavorito/{productId}")
    public ResponseEntity<?> removeFavorite(
            @PathVariable Long productId,
            @RequestHeader("Authorization") String tokenHeader) {
        Long userId = getUserIdFromToken(tokenHeader);
        iUserService.removeFavoriteProduct(userId, productId);
        return new ResponseEntity<>("Producto " + productId + " removido de favoritos", HttpStatus.OK);
    }

    private Long getUserIdFromToken(String tokenHeader) {
        try {
            if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
                String token = tokenHeader.substring(7);

                if (jwtUtils.isTokenValid(token)) {
                    return jwtUtils.getClaim(token, claims -> claims.get("userId", Long.class));
                }
            }
            throw new RuntimeException("JWT token no existente o inválido");
        } catch (Exception e) {
            throw new RuntimeException("Error extrayendo el UserID del JWT: " + e.getMessage());
        }
    }
}
