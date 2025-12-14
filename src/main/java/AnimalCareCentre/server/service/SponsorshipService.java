package AnimalCareCentre.server.service;

import java.time.LocalDate;
import java.util.*;

import AnimalCareCentre.server.enums.Status;
import org.springframework.stereotype.Service;

import AnimalCareCentre.server.repository.SponsorshipRepository;
import AnimalCareCentre.server.dto.SponsorshipDTO;
import AnimalCareCentre.server.model.*;

@Service
public class SponsorshipService {

  private final SponsorshipRepository sponsorshipRepository;

  public SponsorshipService(SponsorshipRepository sponsorshipRepository) {
    this.sponsorshipRepository = sponsorshipRepository;
  }

    /**
     * Create a new sponsorship
     * @param donor
     * @param animal
     * @param amount
     * @return
     */
  public Sponsorship newSponsorship (User donor, ShelterAnimal animal, float amount) {
      Sponsorship sponsorship = new Sponsorship(donor, animal, amount);
      return sponsorshipRepository.save(sponsorship);

  }

    /**
     * To cancel an ongoing sponsorship
     * @param sponsorshipId
     */
  public void cancelSponsorship (Long sponsorshipId) {
      Sponsorship sponsorship = sponsorshipRepository.findById(sponsorshipId).orElse(null);
      if (sponsorship != null) {
          sponsorship.setStatus(Status.CANCELLED);
          sponsorship.setEndDate(LocalDate.now());
          sponsorshipRepository.save( sponsorship );
      }
  }

    /**
     * To get the sponsorship of a certain user
     * @param donor
     * @return
     */
  public List<Sponsorship> getUserSponsorships(User donor) {
      return sponsorshipRepository.findByUserOrderByStartDateDesc(donor);
  }

    /**
     * To get the sponsorships of a certain animal
     * @param animal
     * @return
     */
  public List<Sponsorship> getAnimalSponsorships(ShelterAnimal animal) {
      return sponsorshipRepository.findByAnimalOrderByStartDateDesc(animal);
  }

    /**
     * To get all the sponsorships
     * @return
     */
  public List<Sponsorship> getAllSponsorships() {
      return sponsorshipRepository.findAll();
  }

    /**
     * Verifies the number of sponsors of an animal
     * @param animal
     * @return
     */
  public long countActiveSponsors(ShelterAnimal animal) {
      return sponsorshipRepository.countByAnimalAndStatus(animal, Status.ACTIVE);
  }
}
