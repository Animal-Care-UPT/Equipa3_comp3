package AnimalCareCentre.client.records;

import java.time.LocalDate;
import java.util.List;

public record Sponsorship(long id, User user, AnimalCareCentre.server.model.ShelterAnimal animal, LocalDate startDate, Float amount, List<Donation> donations) {}
