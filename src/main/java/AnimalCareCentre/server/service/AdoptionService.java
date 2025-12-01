package AnimalCareCentre.server.service;

import AnimalCareCentre.server.dto.AdoptionResponseDTO;
import AnimalCareCentre.server.dto.AdoptionsUserDTO;
import AnimalCareCentre.server.enums.AdoptionType;
import org.springframework.stereotype.Service;

import AnimalCareCentre.server.repository.AdoptionRepository;
import AnimalCareCentre.server.model.Adoption;
import AnimalCareCentre.server.model.ShelterAnimal;
import AnimalCareCentre.server.model.User;
import AnimalCareCentre.server.enums.Status;

import java.time.LocalDate;
import java.util.List;

@Service
public class AdoptionService {

  private final AdoptionRepository adoptionRepository;
  private final ShelterAnimalService shelterAnimalService;

  public AdoptionService(AdoptionRepository adoptionRepository, ShelterAnimalService shelterAnimalService,
      UserService userService) {
    this.adoptionRepository = adoptionRepository;
    this.shelterAnimalService = shelterAnimalService;
  }

  // To make an adoption
  public Adoption requestAdoption(User user, Long animalId, AdoptionType adoptionType) {
    ShelterAnimal animal = shelterAnimalService.findShelterAnimalById(animalId);
    Adoption adoption = new Adoption();
    adoption.setUser(user);
    adoption.setAnimal(animal);
    adoption.setAdoptionType(adoptionType);
    adoption.setStatus(Status.PENDING);
    adoption.setRequestDate(LocalDate.now());

    adoptionRepository.save(adoption);

    animal.setStatus(Status.UNAVAILABLE);
    shelterAnimalService.save(animal);

    return adoption;

  }

  // To change the status of a pending adoption request
  public void changeStatus(Adoption adoption, Status newStatus) {

    // If the request is rejected we delete it
    if (newStatus == Status.REJECTED) {
      adoptionRepository.delete(adoption);
      return;
    }

    adoption.setStatus(newStatus);

    if (newStatus == Status.ACCEPTED) {
      adoption.setAdoptionDate(LocalDate.now());
    }

    adoptionRepository.save(adoption);
  }

  // So the users can see their adoptions request historic
  public List<AdoptionsUserDTO> getUserAdoptions(User user) {

    List<Adoption> adoptions = adoptionRepository.findByUser(user);

    return adoptions.stream().map(a -> {
      AdoptionsUserDTO dto = new AdoptionsUserDTO();
      dto.setAdoptionId(a.getId());
      dto.setAnimalId(a.getAnimal().getId());
      dto.setAnimalName(a.getAnimal().getName());
      dto.setAdoptionType(a.getType());
      dto.setStatus(a.getStatus());
      dto.setRequestDate(a.getRequestDate());

      // In case the adoption request has been accepted
      if (a.getStatus() == Status.ACCEPTED) {
        dto.setAdoptionDate(a.getAdoptionDate());
      }

      return dto;
    }).toList();
  }

  // So the shelters can see their pending requests
  public List<AdoptionResponseDTO> getPendingRequestsByShelter(Long shelterId) {
    List<Adoption> adoptions = adoptionRepository.findByAnimalShelterIdAndStatus(shelterId, Status.PENDING);

    return adoptions.stream().map(a -> {
      AdoptionResponseDTO dto = new AdoptionResponseDTO();
      dto.setShelterId(a.getAnimal().getShelter().getId());
      dto.setAnimalId(a.getAnimal().getId());
      dto.setAnimalName(a.getAnimal().getName());
      dto.setUserId(a.getUser().getId());
      dto.setAdoptionType(a.getType());
      dto.setStatus(a.getStatus());
      dto.setRequestDate(a.getRequestDate());
      return dto;
    }).toList();
  }

  public Adoption findAdoptionById(Long id) {
    return adoptionRepository.findById(id).orElse(null);
  }
}
