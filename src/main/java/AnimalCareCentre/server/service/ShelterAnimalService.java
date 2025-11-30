package AnimalCareCentre.server.service;

import AnimalCareCentre.server.enums.*;
import AnimalCareCentre.server.model.Shelter;
import AnimalCareCentre.server.model.ShelterAnimal;

import java.util.List;
import org.springframework.stereotype.Service;

import AnimalCareCentre.server.repository.ShelterAnimalRepository;

@Service
public class ShelterAnimalService {

  private final ShelterAnimalRepository shelterAnimalRepository;

  public ShelterAnimalService(ShelterAnimalRepository shelterAnimalRepository) {
    this.shelterAnimalRepository = shelterAnimalRepository;
  }

  public ShelterAnimal registerShelterAnimal(ShelterAnimal shelterAnimal) {
<<<<<<< HEAD
      shelterAnimal.setStatus(Status.AVAILABLE);
      return shelterAnimalRepository.save(shelterAnimal);
=======
    shelterAnimal.setStatus(Status.AVAILABLE);
    return shelterAnimalRepository.save(shelterAnimal);
>>>>>>> 16e186a331ca81e617edb8f74bde3d453e235be3
  }

  public List<ShelterAnimal> searchByKeyword(String search) {
    String keyword = "%" + search + "%";
    return shelterAnimalRepository.findByKeyword(keyword, Status.AVAILABLE);
  }

  public List<ShelterAnimal> searchByGender(AnimalGender gender) {
    return shelterAnimalRepository.findByGenderAndStatus(gender, Status.AVAILABLE);
  }

  public List<ShelterAnimal> searchByType(AnimalType type) {
    return shelterAnimalRepository.findByTypeAndStatus(type, Status.AVAILABLE);
  }

  public List<ShelterAnimal> searchAvailableByShelter(Shelter shelter) {
    return shelterAnimalRepository.findByStatusAndShelter(Status.AVAILABLE, shelter);
  }

  public List<ShelterAnimal> searchByShelter(Shelter shelter) {
    return shelterAnimalRepository.findByShelter(shelter);
  }

  public List<ShelterAnimal> searchAll() {
    return shelterAnimalRepository.findAll();
  }

  public List<ShelterAnimal> searchFosterAnimals() {
    return shelterAnimalRepository.findByAdoptionTypeAndStatus(AdoptionType.FOR_FOSTER,
        Status.AVAILABLE);
  }

  public List<ShelterAnimal> searchAdoptionAnimals() {
    return shelterAnimalRepository.findByAdoptionTypeAndStatus(AdoptionType.FOR_ADOPTION, Status.AVAILABLE);
  }

  public ShelterAnimal findShelterAnimalById(Long id) {
      return shelterAnimalRepository.findById(id).orElse(null);
    }

  //So that we can change the status of a shelteranimal after the adoption
  public void save(ShelterAnimal animal) {
      shelterAnimalRepository.save(animal);
  }


}
