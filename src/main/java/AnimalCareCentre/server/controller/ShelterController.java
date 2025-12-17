package AnimalCareCentre.server.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
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

import AnimalCareCentre.server.dto.ChangeStatusDTO;
import AnimalCareCentre.server.enums.*;
import AnimalCareCentre.server.model.Shelter;
import AnimalCareCentre.server.service.AccountService;
import AnimalCareCentre.server.service.ShelterService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/shelters/")
public class ShelterController {

  private final ShelterService shelterService;
  private final AccountService accountService;

  public ShelterController(ShelterService shelterService, AccountService accountService) {
    this.shelterService = shelterService;
    this.accountService = accountService;
  }

  @PostMapping("/create")
  public ResponseEntity<?> createShelter(@Valid @RequestBody Shelter shelter) {

    if (shelter.getFoundationYear() > LocalDate.now().getYear()) {
      return ResponseEntity.badRequest().body("Foundation year cannot be in the future!");
    }

    String pwError = accountService.verifyPasswordRules(shelter.getPassword());
    if (pwError != null) {
      return ResponseEntity.badRequest().body(pwError);
    }

    if (accountService.findAccount(shelter.getEmail()) != null) {
      return ResponseEntity.status(409).body("Email already registered!");
    }
    Shelter s = shelterService.createShelter(shelter);
    s.setPassword(null);
    return ResponseEntity.status(201).body(s);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/{id}")
  public ResponseEntity<?> getShelterById(@PathVariable Long id) {
    Shelter shelter = shelterService.findById(id);
    if (shelter == null) {
      return ResponseEntity.status(404).body("Shelter with ID " + id + " not found!");
    }
    shelter.setPassword(null);
    return ResponseEntity.ok(shelter);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/status")
  public ResponseEntity<?> changeStatus(@RequestParam long id, @Valid @RequestBody ChangeStatusDTO status) {
    Shelter shelter = shelterService.findById(id);
    if (shelter != null) {
      shelterService.changeStatus(shelter, status.getStatus());
      return ResponseEntity.ok().body("Changed shelter status successfully");
    }
    return ResponseEntity.status(404).body("Shelter not found!");
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/pending")
  public ResponseEntity<?> getPendingShelters() {
    List<Shelter> shelters = shelterService.getPendingShelters();
    if (shelters.isEmpty()) {
      return ResponseEntity.status(404).body("There are no pending Shelters!");
    }
    return ResponseEntity.ok(shelters);
  }

  @PreAuthorize("hasRole('USER')")
  @GetMapping
  public ResponseEntity<?> searchAvailableShelters(@RequestParam String keyword) {
    List<Shelter> shelters = shelterService.searchAvailableShelters(keyword);
    if (shelters.isEmpty()) {
      return ResponseEntity.status(404).body("There are no results!");
    }
    return ResponseEntity.ok(shelters);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/all")
  public ResponseEntity<?> searchAllShelters(@RequestParam String keyword) {
    List<Shelter> shelters = shelterService.searchAllShelters(keyword);
    if (shelters.isEmpty()) {
      return ResponseEntity.status(404).body("There are no results!");
    }
    return ResponseEntity.ok(shelters);
  }

  @GetMapping("/isAvailable")
  public ResponseEntity<?> checkIfAvailable() {
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    Shelter shelter = shelterService.findByEmail(email);
    if (shelter == null) {
      return ResponseEntity.status(404).body("Shelter not registered");
    }
    if (shelter.getStatus() == Status.PENDING) {
      return ResponseEntity.status(404).body("Your request to join the platform is still pending!");
    } else if (shelter.getStatus() == Status.BANNED) {
      return ResponseEntity.status(404).body("You have been banned from Animal Care Centre!");
    } else {
      return ResponseEntity.ok("Welcome!");
    }
  }

  @PostMapping("/{id}/images")
  public ResponseEntity<?> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {

    if (file.isEmpty()) {
      return ResponseEntity.badRequest().body("No file provided");
    }

    Shelter shelter = shelterService.findById(id);
    if (shelter == null) {
      return ResponseEntity.status(404).body("Shelter not found");
    }

    try {
      int index = shelter.getImages().size();
      String extension = FilenameUtils.getExtension(file.getOriginalFilename());
      String filename = "shelter" + id + "_" + index + "." + extension;

      String uploadPath = "src/main/resources/images/shelters/";
      File uploadDir = new File(uploadPath);
      FileUtils.forceMkdir(uploadDir);

      File destFile = new File(uploadDir, filename);
      FileUtils.copyInputStreamToFile(file.getInputStream(), destFile);

      String imageUrl = "/shelters/" + id + "/images/" + index;
      shelterService.addImagePath(shelter, imageUrl);

      return ResponseEntity.ok("Image uploaded successfully");

    } catch (IOException e) {
      return ResponseEntity.status(500).body("Failed to upload image: " + e.getMessage());
    }
  }

  @GetMapping("/{id}/images/{index}")
  public ResponseEntity<?> getImage(@PathVariable Long id, @PathVariable int index) {

    Shelter shelter = shelterService.findById(id);
    if (shelter == null) {
      return ResponseEntity.status(404).body("Shelter not found");
    }

    if (index < 0 || index >= shelter.getImages().size()) {
      return ResponseEntity.status(404).body("Image not found");
    }

    try {
      String uploadPath = "src/main/resources/images/shelters/";
      File imageDir = new File(uploadPath);

      String filePattern = "shelter" + id + "_" + index + ".";
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

  @PreAuthorize("hasRole('SHELTER')")
  @PostMapping("/images/self")
  public ResponseEntity<?> uploadImageToSelf(@RequestParam("file") MultipartFile file) {

    if (file.isEmpty()) {
      return ResponseEntity.badRequest().body("No file provided");
    }

    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    Shelter shelter = shelterService.findByEmail(email);
    if (shelter == null) {
      return ResponseEntity.status(404).body("Shelter not found");
    }

    try {
      int index = shelter.getImages().size();
      String extension = FilenameUtils.getExtension(file.getOriginalFilename());
      String filename = "shelter" + shelter.getId() + "_" + index + "." + extension;

      String uploadPath = "src/main/resources/images/shelters/";
      File uploadDir = new File(uploadPath);
      FileUtils.forceMkdir(uploadDir);

      File destFile = new File(uploadDir, filename);
      FileUtils.copyInputStreamToFile(file.getInputStream(), destFile);

      String imageUrl = "/shelters/" + shelter.getId() + "/images/" + index;
      shelterService.addImagePath(shelter, imageUrl);
      shelterService.save(shelter);

      return ResponseEntity.ok("Image uploaded successfully");

    } catch (IOException e) {
      return ResponseEntity.status(500).body("Failed to upload image: " + e.getMessage());
    }
  }

  /**
   * Returns all images from a shelter
   */
  @GetMapping("/{id}/images")
  public ResponseEntity<?> getAllImages(@PathVariable Long id) {
    Shelter shelter = shelterService.findById(id);
    if (shelter == null) {
      return ResponseEntity.status(404).body("Shelter not found");
    }
    try {
      String uploadPath = "src/main/resources/images/shelters/";
      File imageDir = new File(uploadPath);
      List<String> base64Images = new ArrayList<>();
      int imageCount = shelter.getImages().size();

      for (int i = 0; i < imageCount; i++) {
        String filePattern = "shelter" + id + "_" + i + ".";
        File imageFile = null;

        for (File file : imageDir.listFiles()) {
          if (file.getName().startsWith(filePattern)) {
            imageFile = file;
            break;
          }
        }

        if (imageFile != null && imageFile.exists()) {
          byte[] imageBytes = FileUtils.readFileToByteArray(imageFile);
          String base64Image = Base64.getEncoder().encodeToString(imageBytes);
          base64Images.add(base64Image);
        } else {
          return ResponseEntity.status(404).body("There are no images!");
        }
      }

      return ResponseEntity.ok(base64Images);
    } catch (IOException e) {
      return ResponseEntity.status(500).body("Failed to read images: " + e.getMessage());
    }
  }

}
