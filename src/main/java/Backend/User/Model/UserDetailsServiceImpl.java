package Backend.User.Model;

import Backend.User.Crud.UserRepository;
import com.amazonaws.services.kms.model.DisabledException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario " + username + " no existe."));

        if (!userEntity.isVerified()) {
            throw new DisabledException("Usuario no verificado. Por favor, verifica tu correo electrónico.");
        }

        Collection<? extends GrantedAuthority> authorities = Stream.of(new SimpleGrantedAuthority((userEntity.getRole().name())))
                .map(role -> new SimpleGrantedAuthority("ROLE_".concat(role.getAuthority())))
                .collect(Collectors.toSet());

        return new User(userEntity.getUsername(),
                userEntity.getPassword(),
                true,
                true,
                true,
                true,
                authorities);
    }
}
