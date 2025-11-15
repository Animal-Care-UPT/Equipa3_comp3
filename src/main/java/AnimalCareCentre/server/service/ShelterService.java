package AnimalCareCentre.server.service;

import org.springframework.stereotype.Service;

import AnimalCareCentre.server.model.Shelter;
import AnimalCareCentre.server.repository.ShelterRepository;
import AnimalCareCentre.server.util.ACCPasswordEncryption;

@Service
public class ShelterService {

  private final ShelterRepository shelterRepository;

  public ShelterService(ShelterRepository shelterRepository) {
    this.shelterRepository = shelterRepository;
  }

  public Shelter createShelter(Shelter shelter) {
    shelter.setPassword(ACCPasswordEncryption.encrypt(shelter.getPassword()));
    return shelterRepository.save(shelter);
  }

}
