package AnimalCareCentre.server.controller;

import AnimalCareCentre.server.dto.SearchAnimalDTO;
import AnimalCareCentre.server.model.Shelter;
import AnimalCareCentre.server.model.ShelterAnimal;
import AnimalCareCentre.server.service.ShelterService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import AnimalCareCentre.server.service.ShelterAnimalService;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/shelteranimals/")
public class ShelterAnimalController {

  private final ShelterAnimalService shelterAnimalService;
  private final ShelterService shelterService;

  public ShelterAnimalController(ShelterAnimalService shelterAnimalService,
      ShelterService shelterService) {
    this.shelterAnimalService = shelterAnimalService;
    this.shelterService = shelterService;
  }

  @PreAuthorize("hasRole('SHELTER')")
  @PostMapping("/register")
  public ResponseEntity<?> registerShelterAnimal(@Valid @RequestBody ShelterAnimal shelterAnimal) {
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    Shelter shelter = shelterService.findByEmail(email);
    if (shelter == null) {
      return ResponseEntity.status(404).body("The shelter doesn't exist!");
    }

    shelterAnimal.setShelter(shelter);
    ShelterAnimal savedAnimal = shelterAnimalService.registerShelterAnimal(shelterAnimal);
    return ResponseEntity.status(201).body(savedAnimal);
  }

  @GetMapping("/search/all")
  public ResponseEntity<?> getAnimals() {
    List<ShelterAnimal> results = shelterAnimalService.searchAll();
    if (!results.isEmpty()) {
      return ResponseEntity.ok().body(results);
    }
    return ResponseEntity.status(404).body("There are no registered animals");
  }

  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  @PostMapping("/search")
  public ResponseEntity<?> getAnimalsByKeyword(@RequestBody SearchAnimalDTO search) {
    List<ShelterAnimal> results = shelterAnimalService.searchWithFilters(search);
    if (!results.isEmpty()) {
      return ResponseEntity.ok().body(results);
    }
    return ResponseEntity.status(404).body("No matching animals!");
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/search/shelter")
  public ResponseEntity<?> getShelterAnimals(@NotNull @RequestParam Long id) {
    Shelter shelter = shelterService.findById(id);
    if (shelter == null) {
      return ResponseEntity.status(404).body("Shelter not found!");
    }

    List<ShelterAnimal> animals = shelterAnimalService.searchByShelter(shelter);
    if (!animals.isEmpty()) {
      return ResponseEntity.ok().body(animals);
    }
    return ResponseEntity.status(404).body("No available Animals!");
  }

  @PreAuthorize("hasRole('SHELTER')")
  @GetMapping("/search/self")
  public ResponseEntity<?> getLoggedShelterAnimals() {
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    Shelter shelter = shelterService.findByEmail(email);
    if (shelter == null) {
      return ResponseEntity.status(404).body("Shelter not found!");
    }

    List<ShelterAnimal> animals = shelterAnimalService.searchByShelter(shelter);
    if (!animals.isEmpty()) {
      return ResponseEntity.ok().body(animals);
    }
    return ResponseEntity.status(404).body("No available Animals!");
  }

  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  @GetMapping("/search/shelter/available")
  public ResponseEntity<?> getAvailableShelterAnimals(@NotNull @RequestParam long id) {
    Shelter shelter = shelterService.findById(id);
    if (shelter == null) {
      return ResponseEntity.status(404).body("Shelter not found!");
    }

    List<ShelterAnimal> animals = shelterAnimalService.searchAvailableByShelter(shelter);
    if (!animals.isEmpty()) {
      return ResponseEntity.ok().body(animals);
    }
    return ResponseEntity.status(404).body("This shelter has no available animals!");
  }

  @PreAuthorize("hasRole('SHELTER')")
  @PutMapping("/vacination")
  public ResponseEntity<?> changeVacination(@NotNull @RequestParam Long id) {
    ShelterAnimal animal = shelterAnimalService.findShelterAnimalById(id);
    if (animal != null) {
      shelterAnimalService.changeVacination(animal);
      return ResponseEntity.ok("Changed vacination with success!");
    }
    return ResponseEntity.status(404).body("Animal not found!");
  }

  @PreAuthorize("hasRole('SHELTER')")
  @PutMapping("/age")
  public ResponseEntity<?> changeAge(@NotNull @RequestParam Long id, @NotNull @RequestParam Integer age) {
    ShelterAnimal animal = shelterAnimalService.findShelterAnimalById(id);
    if (animal != null) {
      shelterAnimalService.changeAge(animal, age);
      return ResponseEntity.ok("Changed age with success!");
    }
    return ResponseEntity.status(404).body("Animal not found!");
  }

  @PreAuthorize("hasRole('SHELTER')")
  @PutMapping("/status")
  public ResponseEntity<?> changeStatus(@NotNull @RequestParam Long id) {
    ShelterAnimal animal = shelterAnimalService.findShelterAnimalById(id);
    if (animal != null) {
      shelterAnimalService.changeStatus(animal);
      return ResponseEntity.ok("Changed status with success!");
    }
    return ResponseEntity.status(404).body("Animal not found!");
  }

  @PreAuthorize("hasRole('SHELTER')")
  @PutMapping("/adoptiontype")
  public ResponseEntity<?> changeAdoptionType(@NotNull @RequestParam Long id) {
    ShelterAnimal animal = shelterAnimalService.findShelterAnimalById(id);
    if (animal != null) {
      shelterAnimalService.changeAdoptionType(animal);
      return ResponseEntity.ok("Changed adoption type with success!");
    }
    return ResponseEntity.status(404).body("Animal not found!");
  }

  @PreAuthorize("hasRole('SHELTER')")
  @GetMapping("/search/byid")
  public ResponseEntity<?> getAnimalById(@NotNull @RequestParam Long id) {
    ShelterAnimal animal = shelterAnimalService.findShelterAnimalById(id);
    if (animal != null) {
      return ResponseEntity.ok().body(animal);
    }
    return ResponseEntity.status(404).body("Animal not found!");
  }

  @PreAuthorize("hasRole('SHELTER')")
  @PostMapping("/{id}/images")
  public ResponseEntity<?> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {

    if (file.isEmpty()) {
      return ResponseEntity.badRequest().body("No file provided");
    }

    ShelterAnimal animal = shelterAnimalService.findShelterAnimalById(id);
    if (animal == null) {
      return ResponseEntity.status(404).body("Animal not found");
    }

    try {
      int index = animal.getImages().size();
      String extension = FilenameUtils.getExtension(file.getOriginalFilename());
      String filename = "shelteranimal" + id + "_" + index + "." + extension;

      String uploadPath = "src/main/resources/images/shelteranimals/";
      File uploadDir = new File(uploadPath);
      FileUtils.forceMkdir(uploadDir);

      File destFile = new File(uploadDir, filename);
      FileUtils.copyInputStreamToFile(file.getInputStream(), destFile);

      String imageUrl = "/shelteranimals/" + id + "/images/" + index;
      shelterAnimalService.addImagePath(animal, imageUrl);

      return ResponseEntity.ok("Image uploaded successfully");

    } catch (IOException e) {
      return ResponseEntity.status(500).body("Failed to upload image: " + e.getMessage());
    }
  }

  @GetMapping("/{id}/images/{index}")
  public ResponseEntity<?> getImage(@PathVariable Long id, @PathVariable int index) {

    ShelterAnimal animal = shelterAnimalService.findShelterAnimalById(id);
    if (animal == null) {
      return ResponseEntity.status(404).body("Animal not found");
    }

    if (index < 0 || index >= animal.getImages().size()) {
      return ResponseEntity.status(404).body("Image not found");
    }

    try {
      String uploadPath = "src/main/resources/images/shelteranimals/";
      File imageDir = new File(uploadPath);

      String filePattern = "shelteranimal" + id + "_" + index + ".";
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
