package AnimalCareCentre.server.dto;

import AnimalCareCentre.server.enums.AdoptionType;
import AnimalCareCentre.server.enums.Status;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class AdoptionsUserDTO {

    @NotNull(message = "UserId is mandatory")
    private Long userId;

    private Long adoptionId;

    private Long animalId;

    private String animalName;

    private AdoptionType adoptionType;

    private Status status;

    private LocalDate requestDate;

    private LocalDate adoptionDate;




    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAnimalId() {
        return animalId;
    }

    public void setAnimalId(Long animalId) {
        this.animalId = animalId;
    }

    public String getAnimalName() {
        return animalName;
    }

    public void setAnimalName(String animalName) {
        this.animalName = animalName;
    }

    public AdoptionType getAdoptionType() {
        return adoptionType;
    }

    public void setAdoptionType(AdoptionType adoptionType) {
        this.adoptionType = adoptionType;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public LocalDate getAdoptionDate() {
        return adoptionDate;
    }

    public void setAdoptionDate(LocalDate adoptionDate) {
        this.adoptionDate = adoptionDate;
    }

    public Long getAdoptionId() {
        return adoptionId;
    }

    public void setAdoptionId(Long adoptionId) {
        this.adoptionId = adoptionId;
    }
}
