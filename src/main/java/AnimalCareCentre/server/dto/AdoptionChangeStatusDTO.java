package AnimalCareCentre.server.dto;

import AnimalCareCentre.server.enums.Status;
import jakarta.validation.constraints.NotNull;

public class AdoptionChangeStatusDTO {

    @NotNull(message = "Adoption ID is required")
    private Long adoptionId;

    @NotNull(message = "New status is required")
    private Status newStatus;




    public Long getAdoptionId() {
        return adoptionId;
    }

    public Status getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(Status newStatus) {
        this.newStatus = newStatus;
    }
}
