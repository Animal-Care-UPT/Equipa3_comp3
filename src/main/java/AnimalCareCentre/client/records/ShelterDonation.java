package AnimalCareCentre.client.records;

import java.time.LocalDate;

public record ShelterDonation(
        Long id,
        User user,
        Shelter shelter,
        float amount,
        LocalDate donationDate,
        String donationType
) {

    @Override
    public String toString() {
        return "\nDonation ID: " + id +
                "\nUser: " + (user != null ? user.name() : "N/A") +
                "\nShelter: " + (shelter != null ? shelter.name() : "N/A") +
                "\nAmount: " + amount +
                "\nDate: " + donationDate +
                "\nType: " + donationType + "\n";
    }
}

