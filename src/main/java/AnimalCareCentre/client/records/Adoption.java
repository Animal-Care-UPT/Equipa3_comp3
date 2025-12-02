package AnimalCareCentre.client.records;

import java.io.ObjectInputFilter.Status;
import java.time.LocalDate;

import AnimalCareCentre.client.enums.*;

public record Adoption(long id, User user, ShelterAnimal animal, LocalDate requestDate, LocalDate adoptionDate, AdoptionType type, Status status) {}
