package AnimalCareCentre.client.records;

import AnimalCareCentre.client.enums.SecurityQuestion;
import AnimalCareCentre.client.enums.Status;

public record Shelter(long id, String name, String email, String location, SecurityQuestion securityQuestion, Integer foundationYear, String contact, Status status) {}
