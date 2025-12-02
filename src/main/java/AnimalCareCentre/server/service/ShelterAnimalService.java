package AnimalCareCentre.server.service;

import AnimalCareCentre.server.enums.*;
import AnimalCareCentre.server.model.Shelter;
import AnimalCareCentre.server.model.ShelterAnimal;

import java.util.Comparator;
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
    shelterAnimal.setStatus(Status.AVAILABLE);
    return shelterAnimalRepository.save(shelterAnimal);
  }

  public List<ShelterAnimal> searchByKeyword(String search) {
    String keyword = "%" + search + "%";
    List<ShelterAnimal> animals = shelterAnimalRepository.findByKeyword(keyword, Status.AVAILABLE);
    animals.sort(Comparator.comparing(ShelterAnimal::getType).thenComparing(ShelterAnimal::getRace));
    return animals;
  }

  public List<ShelterAnimal> searchByGender(AnimalGender gender) {
    List<ShelterAnimal> animals = shelterAnimalRepository.findByGenderAndStatus(gender, Status.AVAILABLE);
    animals.sort(Comparator.comparing(ShelterAnimal::getType).thenComparing(ShelterAnimal::getRace));
    return animals;
  }

  public List<ShelterAnimal> searchBySize(AnimalSize size) {
    List<ShelterAnimal> animals = shelterAnimalRepository.findBySizeAndStatus(size, Status.AVAILABLE);
    animals.sort(Comparator.comparing(ShelterAnimal::getType).thenComparing(ShelterAnimal::getRace));
    return animals;
  }

  public List<ShelterAnimal> searchByColor(AnimalColor color) {
    List<ShelterAnimal> animals = shelterAnimalRepository.findByColorAndStatus(color, Status.AVAILABLE);
    animals.sort(Comparator.comparing(ShelterAnimal::getType).thenComparing(ShelterAnimal::getRace));
    return animals;
  }

  public List<ShelterAnimal> searchByType(AnimalType type) {
    List<ShelterAnimal> animals = shelterAnimalRepository.findByTypeAndStatus(type, Status.AVAILABLE);
    animals.sort(Comparator.comparing(ShelterAnimal::getType).thenComparing(ShelterAnimal::getRace));
    return animals;
  }

  public List<ShelterAnimal> searchAvailableByShelter(Shelter shelter) {
    List<ShelterAnimal> animals = shelterAnimalRepository.findByStatusAndShelter(Status.AVAILABLE, shelter);
    animals.sort(Comparator.comparing(ShelterAnimal::getType).thenComparing(ShelterAnimal::getRace));
    return animals;
  }

  public List<ShelterAnimal> searchByShelter(Shelter shelter) {
    List<ShelterAnimal> animals = shelterAnimalRepository.findByShelter(shelter);
    animals.sort(Comparator.comparing(ShelterAnimal::getType).thenComparing(ShelterAnimal::getRace));
    return animals;
  }

  public List<ShelterAnimal> searchAll() {
    List<ShelterAnimal> animals = shelterAnimalRepository.findAll();
    animals.sort(Comparator.comparing(ShelterAnimal::getType).thenComparing(ShelterAnimal::getRace));
    return animals;
  }

  public List<ShelterAnimal> searchFosterAnimals() {
    List<ShelterAnimal> animals = shelterAnimalRepository.findByAdoptionTypeAndStatus(AdoptionType.FOR_FOSTER,
        Status.AVAILABLE);
    animals.sort(Comparator.comparing(ShelterAnimal::getType).thenComparing(ShelterAnimal::getRace));
    return animals;
  }

  public List<ShelterAnimal> searchAdoptionAnimals() {
    List<ShelterAnimal> animals = shelterAnimalRepository.findByAdoptionTypeAndStatus(AdoptionType.FOR_ADOPTION,
        Status.AVAILABLE);
    animals.sort(Comparator.comparing(ShelterAnimal::getType).thenComparing(ShelterAnimal::getRace));
    return animals;
  }

  public ShelterAnimal findShelterAnimalById(Long id) {
    return shelterAnimalRepository.findById(id).orElse(null);
  }

  // So that we can change the status of a shelteranimal after the adoption
  public void save(ShelterAnimal animal) {
    shelterAnimalRepository.save(animal);
  }

  public ShelterAnimal changeVacination(ShelterAnimal animal) {
    if (animal.isVacinated()) {
      animal.setVacinated(false);
    } else {
      animal.setVacinated(true);
    }
    return shelterAnimalRepository.save(animal);
  }

  public ShelterAnimal changeAge(ShelterAnimal animal, int num) {
    animal.setAge(num);
    return shelterAnimalRepository.save(animal);
  }

  public ShelterAnimal changeStatus(ShelterAnimal animal) {
    if (animal.getStatus() == Status.AVAILABLE) {
      animal.setStatus(Status.UNAVAILABLE);
    } else {
      animal.setStatus(Status.AVAILABLE);
    }
    return shelterAnimalRepository.save(animal);
  }

  public ShelterAnimal changeAdoptionType(ShelterAnimal animal) {
    if (animal.getAdoptionType() == AdoptionType.FOR_ADOPTION) {
      animal.setAdoptionType(AdoptionType.FOR_FOSTER);
    } else {
      animal.setAdoptionType(AdoptionType.FOR_ADOPTION);
    }
    return shelterAnimalRepository.save(animal);
  }

}
