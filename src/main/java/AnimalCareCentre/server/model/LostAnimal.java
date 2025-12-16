package AnimalCareCentre.server.model;

import AnimalCareCentre.server.enums.District;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class LostAnimal extends Animal {

    @Enumerated(EnumType.STRING)
  District location;


  String contact;
  @ElementCollection
  List<String> images = new ArrayList<>();


    @ManyToOne
  @JoinColumn(name = "account_id")
  private Account account;

  @Override
  public String toString() {
    return "Location" + location + ", Name=" + getName() + ", Type="
        + getType() + ", Race=" + getRace() + ", Size=" + getSize() + ", Color=" + getColor() + ", Contact: "+contact+ "}";
  }

  public LostAnimal() {
  }

  public String getLocation() {
    return location.name();
  }

  public void setLocation(District location) {
    this.location = location;
  }

  public String getContact() {
    return contact;
  }

  public void setContact(String contact) {
    this.contact = contact;
  }

  public Account getAccount() {
    return account;
  }



    public void setAccount(Account account) {
    this.account = account;
  }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
