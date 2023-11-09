package Backend.Security;
import Backend.Email.EmailService;
import Backend.User.UserDTO;
import Backend.User.UserEntity;
import Backend.User.UserRepository;
import Backend.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthenticationService implements IAuthenticationService{

    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    private final JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private EmailService emailService;

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
            return jwtUtils.generateAccesToken(authenticationRequest.getUsername());
        } catch (AuthenticationException ex) {
            throw new BadRequestException("Error de autenticación");
        }

    }

    @Override
    public String register(UserDTO userDTO) {
        UserEntity userEntity = UserEntity.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .build();


        String verificationToken = UUID.randomUUID().toString();

        userEntity.setVerificationToken(verificationToken);

        userRepository.save(userEntity);


        String verificationLink = "https://your-app-domain/verify?token=" + verificationToken;

        // Send verification email
        emailService.sendVerificationEmail(userDTO.getUsername(), verificationLink);

        return "Usuario registrado con éxito. Se ha enviado un correo de verificación.";
    }

    public boolean verifyAccount(String verificationToken) {
        // Find user by verification token
        UserEntity userEntity = userRepository.findByVerificationToken(verificationToken);

        if (userEntity != null) {
            // Mark the user as verified (you may want to add additional checks here)
            userEntity.setVerified(true);
            userEntity.setVerificationToken(null); // Optional: Clear the verification token after successful verification

            // Save the updated user entity
            userRepository.save(userEntity);

            return true;
        }

        return false;
    }

}
