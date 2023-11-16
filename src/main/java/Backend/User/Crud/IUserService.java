package Backend.User.Crud;

import Backend.User.dto.RoleUpdate;
import Backend.User.dto.UserEntityDTO;
import Backend.User.dto.UserIdentityDTO;

import java.util.List;

public interface IUserService {

    String register (UserEntityDTO userEntityDTO);
    UserEntityDTO create(UserEntityDTO userEntityDTO);

    String deleteById(Long id);

    UserEntityDTO findByid(Long id);

    List<UserIdentityDTO> findAll();

    Boolean existById(Long id);

    String roleUpdate (List<RoleUpdate> roleUpdates);

}

