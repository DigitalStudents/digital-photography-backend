package Backend.User.Crud;

import Backend.User.dto.RoleUpdate;
import Backend.User.dto.UserEntityDTO;
import Backend.User.dto.UserIdentityDTO;
import jakarta.mail.MessagingException;

import java.util.List;

public interface IUserService {

    String register (UserEntityDTO userEntityDTO) throws MessagingException;
    UserEntityDTO create(UserEntityDTO userEntityDTO) throws MessagingException;

    String deleteById(Long id);

    UserEntityDTO findByid(Long id);

    List<UserIdentityDTO> findAll();

    Boolean existById(Long id);

    String roleUpdate (List<RoleUpdate> roleUpdates);

}

