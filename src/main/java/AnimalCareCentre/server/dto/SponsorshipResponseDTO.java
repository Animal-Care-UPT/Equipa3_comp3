package AnimalCareCentre.server.dto;

import jakarta.validation.constraints.NotNull;

public class SponsorshipResponseDTO{
  @NotNull
  private String user;
  @NotNull
  private String animal;
  @NotNull
  private Float amount;

  public String getUser(){
    return user;
  }
  public String getAnimal(){
    return animal;
  }
  public Float getAmount(){
    return amount;
  }
  public void setUser(String user){
    this.user = user;
  }
  public void setAnimal(String animal){
    this.animal = animal;
  }
  public void setAmount(Float amount){
    this.amount = amount;
  }
}
