package Backend.User.Crud;
import Backend.Producto.Producto;
import Backend.Producto.ProductoRepository;
import Backend.Security.JwtUtils;
import Backend.User.Model.ERole;
import Backend.User.Model.UserEntity;
import Backend.User.dto.RoleUpdate;
import Backend.Email.EmailService;
import Backend.User.dto.UserEntityDTO;
import Backend.User.dto.UserIdentityDTO;
import Backend.exceptions.BadRequestException;
import Backend.exceptions.ConflictException;
import com.amazonaws.services.kms.model.NotFoundException;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;
    @Autowired
    private ProductoRepository productoRepository;


    @Override
    public String register(UserEntityDTO userEntityDTO) throws MessagingException {
        createIfNotExist(userEntityDTO);
        return "Se ha enviado un correo de verificación a su Email. Por favor verifique su bandeja de entrada";
    }

    @Override
    public UserEntityDTO create(UserEntityDTO userEntityDTO) throws MessagingException {
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
                    .role(userRepo.getRole())
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

    private UserEntityDTO createIfNotExist(UserEntityDTO userEntityDTO) throws MessagingException {
        Optional<UserEntity> existingUser = userRepository.findByUsername(userEntityDTO.getUsername());

        if (existingUser.isPresent()) {
            throw new ConflictException("Usuario " + userEntityDTO.getUsername() + " ya existe");
        }

        String verificationToken = UUID.randomUUID().toString();
        UserEntity userEntity = UserEntity.builder()
                .firstName(userEntityDTO.getFirstName())
                .lastName(userEntityDTO.getLastName())
                .username(userEntityDTO.getUsername())
                .password(passwordEncoder.encode(userEntityDTO.getPassword()))
                .role(ERole.valueOf(userEntityDTO.getRole().name()))
                .verificationToken(verificationToken)
                .isVerified(false)
                .build();

        userRepository.save(userEntity);

        String verificationLink = "http://localhost:8080/user/auth/verify?token=" + verificationToken;
        emailService.sendVerificationEmail(userEntityDTO.getUsername(), verificationLink);

        return userEntityDTO;
    }

    public boolean verifyAccount(String verificationToken) {
        Optional<UserEntity> optionalUser = userRepository.findByVerificationToken(verificationToken);

        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();
            user.setVerified(true);
            user.setVerificationToken(null);
            userRepository.save(user);
            return true;
        }

        return false;
    }

    @Override
    public void addToFavorites(Long userId, Long productId) {
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        Optional<Producto> productOptional = productoRepository.findById(productId);

        if (userOptional.isPresent() && productOptional.isPresent()) {
            UserEntity user = userOptional.get();
            Producto product = productOptional.get();

            user.getFavoriteProducts().add(product);
            product.getFavoritedByUsers().add(user);

            userRepository.save(user);
            productoRepository.save(product);
        } else {
            throw new NotFoundException("Usario o Producto no encontrado");
        }
    }

    @Override
    public List<Producto> getFavoriteProducts(Long userId) {
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        return userOptional.map(UserEntity::getFavoriteProducts).orElse(new ArrayList<>());
    }

    @Override
    public void removeFavoriteProduct(Long userId, Long productId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Usario no encontrado"));
        Producto product = productoRepository.findById(productId).orElseThrow(() -> new NotFoundException("Producto no encontrado"));

        user.getFavoriteProducts().remove(product);
        userRepository.save(user);
    }
}

