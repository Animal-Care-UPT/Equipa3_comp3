package AnimalCareCentre.server.service;

import AnimalCareCentre.server.dto.AdoptionRequestDTO;
import AnimalCareCentre.server.dto.AdoptionResponseDTO;
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

    //So the shelters can see their pending requests
    public List<AdoptionResponseDTO> getPendingRequestsByShelter(Long shelterId) {
        List<Adoption> adoptions = adoptionRepository.findByAnimalShelterIdAndStatus(shelterId, Status.PENDING);

        return adoptions.stream().map(a-> {
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
