package AnimalCareCentre.server.service;

import org.springframework.stereotype.Service;

import AnimalCareCentre.server.model.User;
import AnimalCareCentre.server.repository.UserRepository;
import AnimalCareCentre.server.util.*;

@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User createUser(User user) {
    user.setPassword(ACCPasswordEncryption.encrypt(user.getPassword()));
    return userRepository.save(user);
  }

  public User changePassword(User user, String password) {
    user.setPassword(ACCPasswordEncryption.encrypt(password));
    return userRepository.save(user);
  }

}
