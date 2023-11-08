package Backend.User;
import Backend.Security.JwtUtils;
import Backend.exceptions.BadRequestException;
import Backend.exceptions.ConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDTO save(UserDTO userDTO) {
        return createIfNotExist(userDTO);
    }

    @Override
    public String deleteById(Long id) {
        if(userRepository.existsById(id)){
            userRepository.deleteById(id);
            return "Se ha borrado usuario con id: "+id;
        }else{
            return "usuario inexistente";
        }
    }

    @Override
    public UserDTO findByid(Long id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);
        if(userEntity.isPresent()){
            UserEntity getUser=userEntity.get();
            UserDTO userDTO= UserDTO.builder()
                    .firstName(getUser.getFirstName())
                    .lastName(getUser.getLastName())
                    .username(getUser.getUsername())
                    .build();
            return userDTO;
        }else{
            throw new BadRequestException("Usuario no encontrado");
        }
    }

    @Override
    public Boolean existById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public List<UserDTO> findAll() {

        List<UserEntity> userEntity= userRepository.findAll();
        List<UserDTO> userDTOS= new ArrayList<>();
        for(UserEntity userRepo: userEntity){
            UserDTO userDTO= UserDTO.builder()
                    .firstName(userRepo.getFirstName())
                    .lastName((userRepo.getLastName()))
                    .username(userRepo.getUsername())
                    .build();

            userDTOS.add(userDTO);
        }

        return userDTOS;
    }

    private UserDTO createIfNotExist(UserDTO userDTO) {

        Set<RoleEntity> roles = userDTO.getRoles().stream()
                .map(role -> RoleEntity.builder()
                        .name(ERole.valueOf(role))
                        .build())
                .collect(Collectors.toSet());
        UserEntity userEntity= UserEntity.builder()
                .username(userDTO.getUsername())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .roles(roles)
                .build();

        if (!userRepository.existsById(1L)) {
            userRepository.save(userEntity);
            return userDTO;
        } else {
            if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
                throw new ConflictException("Usuario "+userDTO.getUsername()+" ya existe");
            } else {
                 userRepository.save(userEntity);
                 return userDTO;
            }
        }
    }


}
