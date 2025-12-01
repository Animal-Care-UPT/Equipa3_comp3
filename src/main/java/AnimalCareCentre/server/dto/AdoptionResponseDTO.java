package AnimalCareCentre.server.dto;

import AnimalCareCentre.server.enums.AdoptionType;
import AnimalCareCentre.server.enums.Status;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class AdoptionResponseDTO {

    @NotNull
    private Long shelterId;

    @NotNull
    private Long animalId;

    private String animalName;

    @NotNull
    private Long userId;

    private AdoptionType adoptionType;

    private Status status;

    private LocalDate requestDate;


    public Long getShelterId() {
        return shelterId;
    }

    public Long getAnimalId() {
        return animalId;
    }

    public String getAnimalName() {
        return animalName;
    }

    public Long getUserId() {
        return userId;
    }

    public AdoptionType getAdoptionType() {
        return adoptionType;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setShelterId(Long shelterId) {
        this.shelterId = shelterId;
    }

    public void setAnimalId(Long animalId) {
        this.animalId = animalId;
    }

    public void setAnimalName(String animalName) {
        this.animalName = animalName;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setAdoptionType(AdoptionType adoptionType) {
        this.adoptionType = adoptionType;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }
}
