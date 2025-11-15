package AnimalCareCentre.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import AnimalCareCentre.server.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

  public Account findByEmail(String email);
  public Account findByEmailAndPassword(String email, String password);
}
