package Backend.User.Crud;

import Backend.Producto.Producto;
import Backend.User.dto.RoleUpdate;
import Backend.User.dto.UserEntityDTO;
import Backend.User.dto.UserIdentityDTO;
import jakarta.mail.MessagingException;

import java.util.List;
import java.util.Set;

public interface IUserService {

<<<<<<< HEAD
    String register (UserEntityDTO userEntityDTO);
=======
    String register (UserEntityDTO userEntityDTO) throws MessagingException;
    UserEntityDTO create(UserEntityDTO userEntityDTO) throws MessagingException;
>>>>>>> 1c438e51880c14d65b231a03c7a6ad6380760560

    String deleteById(Long id);

    UserEntityDTO findByid(Long id);

    List<UserIdentityDTO> findAll();

    Boolean existById(Long id);

    String roleUpdate (List<RoleUpdate> roleUpdates);

    void addToFavorites(Long userId, Long productId);
    void removeFavoriteProduct(Long userId, Long productId);
    List<Producto> getFavoriteProducts(Long userId);


}

