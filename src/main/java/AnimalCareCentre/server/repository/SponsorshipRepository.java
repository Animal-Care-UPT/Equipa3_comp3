package AnimalCareCentre.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import AnimalCareCentre.server.model.Sponsorship;
import AnimalCareCentre.server.model.User;
import AnimalCareCentre.server.model.Shelter;

public interface SponsorshipRepository extends JpaRepository<Sponsorship, Long> {
  public List<Sponsorship> findByUser(User user);
}
