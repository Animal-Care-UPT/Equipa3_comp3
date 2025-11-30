package AnimalCareCentre.server.service;

import java.util.*;
import org.springframework.stereotype.Service;

import AnimalCareCentre.server.repository.SponsorshipRepository;
import AnimalCareCentre.server.model.*;

@Service
public class SponsorshipService {

  private final SponsorshipRepository sponsorshipRepository;

  public SponsorshipService(SponsorshipRepository sponsorshipRepository) {
    this.sponsorshipRepository = sponsorshipRepository;
  }

  public Sponsorship createSponsorShip(User user, ShelterAnimal animal, Float amount) {
    Sponsorship sponsorship = new Sponsorship();
    sponsorship.setUser(user);
    sponsorship.setAnimal(animal);
    sponsorship.setAmount(amount);
    return sponsorshipRepository.save(sponsorship);
  }

  public List<Sponsorship> searchAll(){
    return sponsorshipRepository.findAll(); 
  }
}
