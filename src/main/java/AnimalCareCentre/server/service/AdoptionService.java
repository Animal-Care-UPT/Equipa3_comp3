package AnimalCareCentre.server.service;

import AnimalCareCentre.server.dto.AdoptionDTO;
import AnimalCareCentre.server.enums.AdoptionType;
import AnimalCareCentre.server.model.Shelter;
import org.springframework.stereotype.Service;

import AnimalCareCentre.server.repository.AdoptionRepository;
import AnimalCareCentre.server.model.Adoption;
import AnimalCareCentre.server.model.ShelterAnimal;
import AnimalCareCentre.server.enums.Status;

import AnimalCareCentre.server.model.User;

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
    adoption.setType(adoptionType);
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

  // Pending adoptions
  public List<AdoptionDTO> getUserPendingAdoptions(User user) {
    List<Adoption> adoptions = adoptionRepository.findByUserAndStatus(user, Status.PENDING);

    return adoptions.stream().map(a -> {
      AdoptionDTO dto = new AdoptionDTO();
      dto.setId(a.getId());
      dto.setAnimal(a.getAnimal());
      dto.setUser(a.getUser());
      dto.setType(a.getType());
      dto.setStatus(a.getStatus());
      dto.setRequestDate(a.getRequestDate());
      dto.setAdoptionDate(a.getAdoptionDate());

      return dto;
    }).toList();
  }

  // Accepted adoptions
  public List<AdoptionDTO> getUserAcceptedAdoptions(User user) {
    List<Adoption> adoptions = adoptionRepository.findByUserAndStatus(user, Status.ACCEPTED);

    return adoptions.stream().map(a -> {
      AdoptionDTO dto = new AdoptionDTO();
      dto.setId(a.getId());
      dto.setAnimal(a.getAnimal());
      dto.setUser(a.getUser());
      dto.setType(a.getType());
      dto.setStatus(a.getStatus());
      dto.setRequestDate(a.getRequestDate());
      dto.setAdoptionDate(a.getAdoptionDate());

      return dto;
    }).toList();
  }

  // So the shelters can see their pending requests
  public List<AdoptionDTO> getPendingRequestsByShelter(Shelter shelter) {
    List<Adoption> adoptions = adoptionRepository.findByAnimal_ShelterAndStatus(shelter, Status.PENDING);

    return adoptions.stream().map(a -> {
      AdoptionDTO dto = new AdoptionDTO();
      dto.setId(a.getId());
      dto.setUser(a.getUser());
      dto.setAnimal(a.getAnimal());
      dto.setType(a.getType());
      dto.setStatus(a.getStatus());
      dto.setRequestDate(a.getRequestDate());
      dto.setAdoptionDate(a.getAdoptionDate());

      return dto;
    }).toList();
  }

  public List<AdoptionDTO> getAdoptions() {

    List<Adoption> adoptions = adoptionRepository.findByType(AdoptionType.FOR_ADOPTION);

    return adoptions.stream().map(a -> {
      AdoptionDTO dto = new AdoptionDTO();
      dto.setAnimal(a.getAnimal());
      dto.setUser(a.getUser());
      dto.setType(a.getType());
      dto.setAdoptionDate(a.getAdoptionDate());
      return dto;
    }).toList();
  }

  public List<AdoptionDTO> getFosters() {

    List<Adoption> adoptions = adoptionRepository.findByType(AdoptionType.FOR_FOSTER);

    return adoptions.stream().map(a -> {
      AdoptionDTO dto = new AdoptionDTO();
      dto.setUser(a.getUser());
      dto.setAnimal(a.getAnimal());
      dto.setType(a.getType());
      dto.setAdoptionDate(a.getAdoptionDate());
      return dto;
    }).toList();
  }

  public Adoption findAdoptionById(Long id) {
    return adoptionRepository.findById(id).orElse(null);
  }
}
