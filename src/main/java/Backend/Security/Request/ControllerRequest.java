package Backend.Security.Request;

import Backend.Security.Jwt.JwtUtils;
import Backend.User.Model.ERole;
import Backend.User.Model.RoleEntity;
import Backend.User.Model.UserEntity;
import Backend.User.Repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class ControllerRequest {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@Valid @RequestBody RegisterRequest registerRequest) {
        Set<RoleEntity> roles = registerRequest.getRoles().stream()
                .map(role -> RoleEntity.builder()
                        .name(ERole.valueOf(role))
                        .build())
                .collect(Collectors.toSet());

        UserEntity userEntity= UserEntity.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .roles(roles)
                .build();

        userRepository.save(userEntity);
        RegisterResponse registerResponse= RegisterResponse.builder()
                .token(jwtUtils.generateAccesToken(userEntity.getEmail()))
                .message("Autenticacion Correcta")
                .email(userEntity.getEmail())
                .build();
        return new ResponseEntity<>(registerResponse, HttpStatus.OK);
    }

    @DeleteMapping("/deleteUser")
    public String deleteUser(@RequestParam String id){
        userRepository.deleteById(Long.parseLong(id));
        return "Se ha borrado el user con id".concat(id);
    }
}
