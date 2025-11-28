package AnimalCareCentre.server.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import AnimalCareCentre.server.enums.*;
import AnimalCareCentre.server.model.Shelter;
import AnimalCareCentre.server.repository.ShelterRepository;

@Service
public class ShelterService {

  private final ShelterRepository shelterRepository;
  private final PasswordEncoder passwordEncoder;

  public ShelterService(ShelterRepository shelterRepository, PasswordEncoder passwordEncoder) {
    this.shelterRepository = shelterRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public Shelter findById(Long id) {
    return shelterRepository.findById(id).orElse(null);
  }

  public Shelter findByEmail(String email) {
    return shelterRepository.findByEmail(email);
  }

  public Shelter createShelter(Shelter shelter) {
    shelter.setPassword(passwordEncoder.encode(shelter.getPassword()));
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
    shelter.setPassword(passwordEncoder.encode(password));
    return shelterRepository.save(shelter);
  }

  public List<Shelter> getShelters() {
    return shelterRepository.findByStatus(Status.AVAILABLE);
  }
}
