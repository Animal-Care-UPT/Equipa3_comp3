package AnimalCareCentre.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import AnimalCareCentre.server.enums.Status;
import AnimalCareCentre.server.model.Shelter;

public interface ShelterRepository extends JpaRepository<Shelter, Long> {

  public List<Shelter> findByStatus(Status status);
  public Shelter findByEmail(String email);
}
