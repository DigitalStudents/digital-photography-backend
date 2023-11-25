package Backend.User.Auth;
import Backend.Security.JwtUtils;
import Backend.User.Crud.UserRepository;
import Backend.User.Model.ERole;
import Backend.User.Model.UserEntity;
import Backend.User.dto.UserEntityDTO;
import Backend.exceptions.BadRequestException;
import com.amazonaws.services.kms.model.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService implements IAuthenticationService {

    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    private final JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public String login(AuthenticationRequest authenticationRequest) {
        try {
            UsernamePasswordAuthenticationToken login = new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword());
            Authentication authentication = authenticationManager.authenticate(login);

            UserEntity user = getUser(authenticationRequest.getUsername());

            return jwtUtils.generateAccessToken(authenticationRequest.getUsername(), user.getId(), user.getRole());
        } catch (AuthenticationException ex) {
            throw new BadRequestException("Error de autenticación");
        }
    }

    private UserEntity getUser(String username) {
        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(username);
        return userEntityOptional.orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
    }

}
