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
    return shelterAnimalRepository.save(shelterAnimal);
  }

  public List<ShelterAnimal> searchByKeyword(String search) {
    String keyword = "%" + search + "%";
    List<ShelterAnimal> results = shelterAnimalRepository.findByKeyword(keyword, Status.ACCEPTED);
    if (!results.isEmpty()) {
      return results;
    }
    return null;
  }

  public List<ShelterAnimal> searchByGender(AnimalGender gender) {
    List<ShelterAnimal> results = shelterAnimalRepository.findByGender(gender);
    if (!results.isEmpty()) {
      return results;
    }
    return null;
  }

  public List<ShelterAnimal> searchByType(AnimalType type) {
    List<ShelterAnimal> results = shelterAnimalRepository.findByType(type);
    if (!results.isEmpty()) {
      return results;
    }
    return null;
  }

  public List<ShelterAnimal> searchAvailableByShelter(Shelter shelter) {
    List<ShelterAnimal> results = shelterAnimalRepository.findByStatusAndShelter(Status.ACCEPTED, shelter);
    if (!results.isEmpty()) {
      return results;
    }
    return null;
  }

  public List<ShelterAnimal> searchByShelter(Shelter shelter) {
    List<ShelterAnimal> results = shelterAnimalRepository.findByShelter(shelter);
    if (!results.isEmpty()) {
      return results;
    }
    return null;
  }

  public List<ShelterAnimal> searchAll() {
    List<ShelterAnimal> results = shelterAnimalRepository.findAll();
    if (!results.isEmpty()) {
      return results;
    }
    return null;
  }

}
