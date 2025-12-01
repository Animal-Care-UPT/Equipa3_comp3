package AnimalCareCentre.client.records;

import AnimalCareCentre.client.enums.SecurityQuestion;

public record Account(long id, String name, String email, String location, SecurityQuestion securityQuestion) {}
