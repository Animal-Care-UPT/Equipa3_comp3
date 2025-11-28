package AnimalCareCentre.server.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import AnimalCareCentre.server.model.User;
import AnimalCareCentre.server.repository.UserRepository;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public User createUser(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository.save(user);
  }

  public User changePassword(User user, String password) {
    user.setPassword(passwordEncoder.encode(password));
    return userRepository.save(user);
  }

}
