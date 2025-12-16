package AnimalCareCentre.server.dto;

import AnimalCareCentre.server.model.Sponsorship;
import java.time.LocalDate;

public class SponsorshipDTO {
    private Long sponsorshipId;
    private Long userId;
    private String userName;
    private String userEmail;

    private Long animalId;
    private String animalName;
    private String shelterName;

    private Float amount;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
    private Float totalDonated;


    public SponsorshipDTO() {
    }


    public static SponsorshipDTO fromEntity(Sponsorship sponsorship) {
        SponsorshipDTO dto = new SponsorshipDTO();
        dto.setSponsorshipId(sponsorship.getId());


        if (sponsorship.getUser() != null) {
            dto.setUserId(sponsorship.getUser().getId());
            dto.setUserName(sponsorship.getUser().getName());
            dto.setUserEmail(sponsorship.getUser().getEmail());
        }


        if (sponsorship.getAnimal() != null) {
            dto.setAnimalId(sponsorship.getAnimal().getId());
            dto.setAnimalName(sponsorship.getAnimal().getName());
            if (sponsorship.getAnimal().getShelter() != null) {
                dto.setShelterName(sponsorship.getAnimal().getShelter().getName());
            }
        }


        dto.setAmount(sponsorship.getAmount());
        dto.setStatus(sponsorship.getStatus() != null ? sponsorship.getStatus().toString() : null);
        dto.setStartDate(sponsorship.getStartDate());
        dto.setEndDate(sponsorship.getEndDate());
        dto.setTotalDonated(sponsorship.getTotalDonated());

        return dto;
    }

    // Getters e Setters

    public Long getSponsorshipId() {
        return sponsorshipId;
    }

    public void setSponsorshipId(Long id) {
        this.sponsorshipId = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Long getAnimalId() {
        return animalId;
    }

    public void setAnimalId(Long animalId) {
        this.animalId = animalId;
    }

    public String getAnimalName() {
        return animalName;
    }

    public void setAnimalName(String animalName) {
        this.animalName = animalName;
    }

    public String getShelterName() {
        return shelterName;
    }

    public void setShelterName(String shelterName) {
        this.shelterName = shelterName;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Float getTotalDonated() {
        return totalDonated;
    }

    public void setTotalDonated(Float totalDonated) {
        this.totalDonated = totalDonated;
    }
}