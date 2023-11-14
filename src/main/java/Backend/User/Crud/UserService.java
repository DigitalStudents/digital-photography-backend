package Backend.User.Crud;
import Backend.Security.JwtUtils;
import Backend.User.Model.ERole;
import Backend.User.Model.UserEntity;
import Backend.User.dto.RoleUpdate;
import Backend.User.dto.UserEntityDTO;
import Backend.User.dto.UserIdentityDTO;
import Backend.exceptions.BadRequestException;
import Backend.exceptions.ConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    public String register(UserEntityDTO userEntityDTO) {
        createIfNotExist(userEntityDTO);
        return "usuario guardado con exito";
    }

    @Override
    public UserEntityDTO create(UserEntityDTO userEntityDTO) {
        return createIfNotExist(userEntityDTO);
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
    public UserEntityDTO findByid(Long id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);
        if(userEntity.isPresent()){
            UserEntity getUser=userEntity.get();
            UserEntityDTO userEntityDTO = UserEntityDTO.builder()
                    .firstName(getUser.getFirstName())
                    .lastName(getUser.getLastName())
                    .username(getUser.getUsername())
                    .build();
            return userEntityDTO;
        }else{
            throw new BadRequestException("Usuario no encontrado");
        }
    }

    @Override
    public Boolean existById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public List<UserIdentityDTO> findAll() {

        List<UserEntity> userEntity= userRepository.findAll();
        List<UserIdentityDTO> userEntityDTOS = new ArrayList<>();
        for(UserEntity userRepo: userEntity){
            UserIdentityDTO userEntityDTO = UserIdentityDTO.builder()
                    .id(userRepo.getId())
                    .firstName(userRepo.getFirstName())
                    .lastName((userRepo.getLastName()))
                    .username(userRepo.getUsername())
                    .build();

            userEntityDTOS.add(userEntityDTO);
        }

        return userEntityDTOS;
    }

    @Override
    public String roleUpdate(List<RoleUpdate> roleUpdates) {

        List<Long>ids= roleUpdates.stream().map(RoleUpdate::getId).toList();

        List<UserEntity> userEntityList= userRepository.findAllById(ids);

        int aux=0;
        for(UserEntity user:userEntityList){
            if(roleUpdates.get(aux).isAdmin()){
                user.setRole(ERole.ADMIN);
            }else{
                user.setRole(ERole.USER);
            }
            aux++;
        }
        userRepository.saveAll(userEntityList);
        return "Roles Actualizados";
    }

        private UserEntityDTO createIfNotExist (UserEntityDTO userEntityDTO){

            UserEntity userEntity= UserEntity.builder()
                    .firstName(userEntityDTO.getFirstName())
                    .lastName(userEntityDTO.getLastName())
                    .username(userEntityDTO.getUsername())
                    .password(passwordEncoder.encode(userEntityDTO.getPassword()))
                    .role(ERole.valueOf(userEntityDTO.getRole().name()))
                    .build();

            if (!userRepository.existsById(1L)) {
                userRepository.save(userEntity);
                return userEntityDTO;
            } else {
                if (userRepository.findByUsername(userEntityDTO.getUsername()).isPresent()) {
                    throw new ConflictException("Usuario " + userEntityDTO.getUsername() + " ya existe");
                } else {
                    userRepository.save(userEntity);
                    return userEntityDTO;
                }
            }

    }

}

