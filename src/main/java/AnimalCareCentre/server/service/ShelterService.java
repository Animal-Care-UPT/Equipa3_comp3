package AnimalCareCentre.server.service;

import java.util.List;

import org.springframework.stereotype.Service;

import AnimalCareCentre.server.enums.*;
import AnimalCareCentre.server.model.Shelter;
import AnimalCareCentre.server.repository.ShelterRepository;
import AnimalCareCentre.server.util.ACCPasswordEncryption;

@Service
public class ShelterService {

  private final ShelterRepository shelterRepository;

  public ShelterService(ShelterRepository shelterRepository) {
    this.shelterRepository = shelterRepository;
  }

  public Shelter findById(Long id) {

    return shelterRepository.findById(id).orElse(null);
  }

  public Shelter createShelter(Shelter shelter) {
    shelter.setPassword(ACCPasswordEncryption.encrypt(shelter.getPassword()));
    shelter.setStatus(Status.PENDING);
    return shelterRepository.save(shelter);
  }

  public Shelter changeStatus(Shelter shelter, Status status) {
    shelter.setStatus(status);
    return shelterRepository.save(shelter);
  }

  public List<Shelter> getPendingShelters() {
    return shelterRepository.findByStatus(Status.PENDING);
  }

  public Shelter changePassword(Shelter shelter, String password) {
    shelter.setPassword(ACCPasswordEncryption.encrypt(password));
    return shelterRepository.save(shelter);
  }

  public List<Shelter> getShelters() {
    return shelterRepository.findByStatus(Status.AVAILABLE);
  }
}
