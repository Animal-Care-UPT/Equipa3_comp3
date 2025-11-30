package AnimalCareCentre.server.service;

import AnimalCareCentre.server.enums.AdoptionType;
import org.springframework.stereotype.Service;

import AnimalCareCentre.server.repository.AdoptionRepository;
import AnimalCareCentre.server.model.Adoption;
import AnimalCareCentre.server.model.ShelterAnimal;
import AnimalCareCentre.server.model.User;
import AnimalCareCentre.server.enums.Status;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;


@Service
public class AdoptionService {

  private final AdoptionRepository adoptionRepository;
    private final ShelterAnimalService shelterAnimalService;

    public AdoptionService(AdoptionRepository adoptionRepository, ShelterAnimalService shelterAnimalService) {
    this.adoptionRepository = adoptionRepository;
        this.shelterAnimalService = shelterAnimalService;
    }


    public Adoption requestAdoption(User user, Long animalId, AdoptionType adoptionType) {
        ShelterAnimal animal = shelterAnimalService.findByShelterAnimalById(animalId);
        Adoption adoption = new Adoption();
        adoption.setUser(user);
        adoption.setAnimal(animal);
        adoption.setAdoptionType(adoptionType);
        adoption.setStatus(Status.PENDING);
        adoption.setRequestDate(LocalDate.now());
        return adoptionRepository.save(adoption);
    }


    public Adoption changeStatus(Adoption adoption, Status status) {
        adoption.setStatus(status);
        if (status == Status.ACCEPTED) {
            animalNotAvailable(adoption.getAnimal());
            adoption.setAdoptionDate(LocalDate.now());
        }
        return adoptionRepository.save(adoption);
    }

    private void animalNotAvailable(ShelterAnimal animal) {
        animal.setAdoptionType(AdoptionType.NOT_AVAILABLE);
    }

    public List<Adoption> getUserAdoptions(User user) {
        return adoptionRepository.findByUser(user);
    }

    public List<Adoption> getPendingRequestsByShelter(Long shelterId) {
        return adoptionRepository.findByAnimalShelterIdAndStatus(shelterId, Status.PENDING);
    }

    public Adoption findAdoptionById(Long id) {
        return adoptionRepository.findById(id).orElse(null);
    }
}
