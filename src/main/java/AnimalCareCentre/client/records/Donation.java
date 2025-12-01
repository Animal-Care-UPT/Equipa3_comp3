package AnimalCareCentre.client.records;

import java.time.LocalDate;

public record Donation(long id, float amount, LocalDate donationDate, AnimalCareCentre.server.model.Sponsorship sponsorship) {}
