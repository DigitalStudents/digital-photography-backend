package Backend.User;

import Backend.User.UserDTO;
import Backend.User.UserEntity;

import java.util.List;

public interface IUserService {
    UserDTO save(UserDTO userDTO);
    String deleteById(Long id);
    UserDTO findByid(Long id);
    List<UserDTO> findAll();
    Boolean existById(Long id);

}
