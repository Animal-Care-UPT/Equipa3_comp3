package AnimalCareCentre.server.service;

import java.time.LocalDate;
import java.util.*;
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

  public Sponsorship createSponsorShip(User user, ShelterAnimal animal, Float amount) {
    Sponsorship sponsorship = new Sponsorship();
    sponsorship.setUser(user);
    sponsorship.setAnimal(animal);
    sponsorship.setAmount(amount);
    sponsorship.setStartDate(LocalDate.now());
    return sponsorshipRepository.save(sponsorship);
  }

  public List<SponsorshipDTO> searchAll() {
    List<Sponsorship> sponsors = sponsorshipRepository.findAll();
    return sponsors.stream().map(s -> {
      SponsorshipDTO dto = new SponsorshipDTO();

      if (s.getUser() != null) {
        dto.setUserId(s.getUser().getId());
        dto.setUserName(s.getUser().getName());
      }

      if (s.getAnimal() != null) {
        dto.setAnimalId(s.getAnimal().getId());
        dto.setAnimalName(s.getAnimal().getName());
      }

      dto.setAmount(s.getAmount());
      dto.setStartDate(s.getStartDate());

      return dto;
    }).toList();
  }

  public List<SponsorshipDTO> searchSponsorshipsUser(User user) {
    List<Sponsorship> sponsors = sponsorshipRepository.findByUser(user);
    return sponsors.stream().map(a -> {
      SponsorshipDTO dto = new SponsorshipDTO();
      dto.setUserId(a.getUser().getId());
      dto.setUserName(a.getUser().getName());
      dto.setAnimalName(a.getAnimal().getName());
      dto.setAnimalId(a.getAnimal().getId());
      dto.setStartDate(a.getStartDate());
      dto.setAmount(a.getAmount());
      return dto;
    }).toList();
  }
}
