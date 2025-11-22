package AnimalCareCentre.server.model;

import AnimalCareCentre.server.enums.*;
import jakarta.persistence.*;

@Entity
public class LostAnimal extends Animal {

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
