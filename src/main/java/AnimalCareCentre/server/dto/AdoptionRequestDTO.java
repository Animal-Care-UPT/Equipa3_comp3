package AnimalCareCentre.server.dto;

import AnimalCareCentre.server.enums.AdoptionType;
import AnimalCareCentre.server.model.ShelterAnimal;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

public class AdoptionRequestDTO {

    @NotNull(message = "The must declare the id of the animal")
    private Long animalId;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "You must declare if you adopting or fostering")
    private AdoptionType adoptionType;

    public Long getAnimalId() {
        return animalId;
    }

    public AdoptionType setAdoptionType(AdoptionType adoptionType) {
        return adoptionType;
    }

    public AdoptionType getAdoptionType() {
        return adoptionType;
    }
}
