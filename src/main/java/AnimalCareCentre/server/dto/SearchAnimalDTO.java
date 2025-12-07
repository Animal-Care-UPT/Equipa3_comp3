package AnimalCareCentre.server.dto;

import AnimalCareCentre.server.enums.AdoptionType;
import AnimalCareCentre.server.enums.AnimalGender;
import AnimalCareCentre.server.enums.AnimalType;

public class SearchAnimalDTO {

  private String keyword;
  private AnimalType type;
  private AnimalGender gender;
  private AdoptionType adoptionType;

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public AnimalType getType() {
    return type;
  }

  public void setType(AnimalType type) {
    this.type = type;
  }

  public AnimalGender getGender() {
    return gender;
  }

  public void setGender(AnimalGender gender) {
    this.gender = gender;
  }

  public AdoptionType getAdoptionType() {
    return adoptionType;
  }

  public void setAdoptionType(AdoptionType adoptionType) {
    this.adoptionType = adoptionType;
  }
}
