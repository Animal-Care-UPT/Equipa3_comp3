package AnimalCareCentre.client.records;

import java.time.LocalDate;
import AnimalCareCentre.client.enums.*;

public record Adoption(
        Long shelterId,
        Long animalId,
        String animalName,
        Long userId,
        AdoptionType adoptionType,
        Long adoptionId,
        Status status,
        LocalDate requestDate
) {}

