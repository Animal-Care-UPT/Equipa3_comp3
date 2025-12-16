package AnimalCareCentre.server.controller;

import AnimalCareCentre.server.enums.AnimalColor;
import AnimalCareCentre.server.enums.AnimalType;
import AnimalCareCentre.server.enums.District;
import AnimalCareCentre.server.model.Account;
import AnimalCareCentre.server.model.LostAnimal;
import AnimalCareCentre.server.model.ShelterAnimal;
import AnimalCareCentre.server.service.AccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import AnimalCareCentre.server.service.LostAnimalService;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/lostandfound/")
public class LostAnimalController {

  private final LostAnimalService lostAnimalService;
  private final AccountService accountService;

  public LostAnimalController(LostAnimalService lostAnimalService, AccountService accountService) {
    this.lostAnimalService = lostAnimalService;
    this.accountService = accountService;
  }

 /* @GetMapping("/showrescuedanimals")
  public ResponseEntity<?> showRescuedAnimals() {
    List<LostAnimal> results = lostAnimalService.findRescuedAnimals();
    results.sort(Comparator.comparing(LostAnimal::getLocation));
    if (!results.isEmpty()) {
      return ResponseEntity.ok().body(results);
    }
    return ResponseEntity.status(404).body("There are no registered animals as rescued");
  }

  */

  @GetMapping("/showlostanimals")
  public ResponseEntity<?> showLostAnimals() {
    List<LostAnimal> results = lostAnimalService.findLostAnimals();
    results.sort(Comparator.comparing(LostAnimal::getLocation));
    if (!results.isEmpty()) {
      return ResponseEntity.ok().body(results);
    }
    return ResponseEntity.status(404).body("There are no registered animals in Lost and Found");
  }

  @GetMapping("/showanimalsbyaccount")
  public ResponseEntity<?> showLostAnimalsByAccount() {
    Account account = accountService.findAccount(SecurityContextHolder.getContext().getAuthentication().getName());

    List<LostAnimal> results = lostAnimalService.findLostAnimalsByAccount(account.getId());
    results.sort(Comparator.comparing(LostAnimal::getLocation));
    if (!results.isEmpty()) {
      return ResponseEntity.ok().body(results);
    }
    return ResponseEntity.status(404).body("There are no registered lost animals to this account");
  }

  @GetMapping("/showByLocation")
  public ResponseEntity<?> showLostAnimalsByLocation(@NotNull@RequestParam District location) {

    List<LostAnimal> results = lostAnimalService.findByLocation(location);
    if (!results.isEmpty()) {
      return ResponseEntity.ok().body(results);
    }
    return ResponseEntity.status(404).body("There are no registered lost animals in this location");
  }

  @GetMapping("/showByType")
  public ResponseEntity<?> showLostAnimalsByType(@NotNull @RequestParam AnimalType animalType) {

    List<LostAnimal> results = lostAnimalService.searchByType(animalType);
    if (!results.isEmpty()) {
      return ResponseEntity.ok().body(results);
    }
    return ResponseEntity.status(404).body("There are no registered lost animals in this location");
  }

  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  @GetMapping("/showByColor")
  public ResponseEntity<?> showLostAnimalsByColor(@NotNull @RequestParam AnimalColor animalColor) {

    List<LostAnimal> results = lostAnimalService.searchByColor(animalColor);
    if (!results.isEmpty()) {
      return ResponseEntity.ok().body(results);
    }
    return ResponseEntity.status(404).body("There are no registered lost animals in this location");
  }

  @PostMapping("/create")
  public ResponseEntity<?> registerLostAnimal(@Valid @RequestBody LostAnimal lostAnimal) {
    Account acc = accountService.findAccount(SecurityContextHolder.getContext().getAuthentication().getName());
    if (acc == null) {
      return ResponseEntity.status(404).body("Account not found!");
    }
    lostAnimal.setAccount(acc);
    lostAnimalService.registerLostAnimal(lostAnimal);
    return ResponseEntity.status(200).body(lostAnimal);

  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<?> deleteAnimal(@PathVariable long id) {

    lostAnimalService.deleteById(id);
    return ResponseEntity.status(200).body(id);

  }
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{id}/images")
    public ResponseEntity<?> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("No file provided");
        }

        LostAnimal animal = lostAnimalService.findLostAnimalById(id);
        if (animal == null) {
            return ResponseEntity.status(404).body("Animal not found");
        }

        try {
            int index = animal.getImages().size();
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            String filename = "lostanimal" + id + "_" + index + "." + extension;

            String uploadPath = "src/main/resources/images/lostanimals/";
            File uploadDir = new File(uploadPath);
            FileUtils.forceMkdir(uploadDir);

            File destFile = new File(uploadDir, filename);
            FileUtils.copyInputStreamToFile(file.getInputStream(), destFile);

            String imageUrl = "/lostanimals/" + id + "/images/" + index;
            lostAnimalService.addImagePath(animal, imageUrl);

            return ResponseEntity.ok("Image uploaded successfully");

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload image: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}/images/{index}")
    public ResponseEntity<?> getImage(@PathVariable Long id, @PathVariable int index) {

        LostAnimal animal = lostAnimalService.findLostAnimalById(id);
        if (animal == null) {
            return ResponseEntity.status(404).body("Animal not found");
        }

        if (index < 0 || index >= animal.getImages().size()) {
            return ResponseEntity.status(404).body("Image not found");
        }

        try {
            String uploadPath = "src/main/resources/images/lostanimals/";
            File imageDir = new File(uploadPath);

            String filePattern = "lostanimal" + id + "_" + index + ".";
            File imageFile = null;

            for (File file : imageDir.listFiles()) {
                if (file.getName().startsWith(filePattern)) {
                    imageFile = file;
                    break;
                }
            }

            if (imageFile == null || !imageFile.exists()) {
                return ResponseEntity.status(404).body("Image file not found");
            }

            byte[] imageBytes = FileUtils.readFileToByteArray(imageFile);
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            return ResponseEntity.ok(base64Image);

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to read image: " + e.getMessage());
        }
    }

}
