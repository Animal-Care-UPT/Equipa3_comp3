package AnimalCareCentre.client.records;

import AnimalCareCentre.client.enums.*;

public record LostAnimal(long id, String name, AnimalType type, String race, AnimalSize size, AnimalGender gender, AnimalColor color, String description, String location, boolean isLost, int contact) {
    @Override
    public String toString() {
        return
                name + '\n' +
                ", type=" + type +
                ", race='" + race + '\'' +
                ", size=" + size +
                ", gender=" + gender +
                ", color=" + color +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", contact=" + contact +
                '\n';
    }
}
