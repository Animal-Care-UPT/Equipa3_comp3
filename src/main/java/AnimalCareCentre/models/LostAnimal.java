package AnimalCareCentre.models;

import AnimalCareCentre.enums.AnimalColor;
import AnimalCareCentre.enums.AnimalGender;
import AnimalCareCentre.enums.AnimalSize;
import AnimalCareCentre.enums.AnimalType;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class LostAnimal extends Animal {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  String location;

  boolean isLost;

  int contact;

  @ManyToOne
  @JoinColumn(name = "account_id")
  private Account account;

  @Override
  public String toString() {
    return "LostAnimal{location=" + location + ", Name=" + getName() + ", Type="
        + getType() + ", Race=" + getRace() + ", Size=" + getSize() + ", Color=" + getColor() + "}";
  }

  public LostAnimal() {
  }

  public LostAnimal(String name, AnimalType type, String race, AnimalColor color, AnimalSize size, AnimalGender gender, String description,
                    int contact, String location) {
    super(name, type, race, size, gender, color, description);
    this.location = location;
    this.contact = contact;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public int getContact() {
    return contact;
  }

  public void setContact(int contact) {
    this.contact = contact;
  }

  public Account getAccount() {
    return account;
  }

  public void setAccount(Account account) {
    this.account = account;
  }

  public boolean isLost() {
    return isLost;
  }

  public void setLost(boolean lost) {
    isLost = lost;
  }
}
