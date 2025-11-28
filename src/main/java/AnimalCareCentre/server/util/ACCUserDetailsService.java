package AnimalCareCentre.server.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import AnimalCareCentre.server.model.*;
import AnimalCareCentre.server.service.AccountService;

@Service
public class ACCUserDetailsService implements UserDetailsService {

  @Autowired
  private AccountService accountService;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Account account = accountService.findAccount(email);

    if (account == null) {
      throw new UsernameNotFoundException("Account not found");
    }

    String role;
    if (account instanceof Shelter) {
      role = "SHELTER";
    } else if (account instanceof User) {
      role = "USER";
    } else {
      role = "ADMIN";
    }

    return org.springframework.security.core.userdetails.User.builder()
        .username(account.getEmail())
        .password(account.getPassword())
        .authorities("ROLE_" + role)
        .build();
  }
}
