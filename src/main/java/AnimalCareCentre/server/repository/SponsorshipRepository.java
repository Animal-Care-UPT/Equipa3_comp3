package AnimalCareCentre.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import AnimalCareCentre.server.model.Sponsorship;

public interface SponsorshipRepository extends JpaRepository<Sponsorship, Long> {
  
}
