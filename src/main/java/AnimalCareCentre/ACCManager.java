package AnimalCareCentre;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.validator.routines.EmailValidator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

import AnimalCareCentre.enums.*;
import AnimalCareCentre.models.*;

public class ACCManager {

  public ACCManager() {
    setup();
  }

  private static SessionFactory sessionFactory;
  private static Session session;

  public void adoptAnimal(User user, ShelterAnimal animal, AdoptionType type) {
    session.beginTransaction();
    Adoption adopt = new Adoption(user, animal, type);
    animal.setListedFor(AdoptionType.NOT_AVAILABLE);
    session.merge(animal);
    session.persist(adopt);
    session.getTransaction().commit();
  }

  public void createSponsorship(User user, ShelterAnimal animal, float amount) {
    session.beginTransaction();
    Sponsorship sponsor = new Sponsorship(user, animal, amount);
    session.persist(sponsor);
    session.getTransaction().commit();
  }

  public List<Adoption> getUserAdoptions(User user) {
    Query<Adoption> query = session.createQuery("FROM Adoption WHERE user = :user");
    query.setParameter("user", user);
    return query.getResultList();
  }

  /**
   * This method searches animals by a keyword
   *
   * @param search
   * @return
   */
  public List<ShelterAnimal> searchAnimalByKeyword(String search) {
    Query<ShelterAnimal> query = session.createQuery(
        "FROM ShelterAnimal WHERE listedFor != :status AND (" +
            "name LIKE :search OR race LIKE :search OR " +
            "CAST(type AS string) LIKE :search OR " +
            "CAST(size AS string) LIKE :search OR " +
            "CAST(color AS string) LIKE :search)",
        ShelterAnimal.class);
    query.setParameter("search", "%" + search + "%");
    query.setParameter("status", AdoptionType.NOT_AVAILABLE);
    return query.getResultList();
  }

  public List<Shelter> searchShelters() {
    Query<Shelter> query = session.createQuery("FROM Shelter", Shelter.class);
    return query.getResultList();
  }

  /**
   * This method searches animals by parameter
   *
   * @param parameter
   * @param search
   * @return
   */
  public List<ShelterAnimal> searchAnimalByParameter(String parameter, Object search) {
    Query<ShelterAnimal> query = session.createQuery(
        "From ShelterAnimal WHERE " + parameter + " =:search AND listedFor != :status", ShelterAnimal.class);
    query.setParameter("search", search);
    query.setParameter("status", AdoptionType.NOT_AVAILABLE);
    return query.getResultList();
  }

  public void changePassword(String email, String password) {
    session.beginTransaction();
    Query<Account> query = session.createQuery("FROM Account WHERE email =:email", Account.class);
    query.setParameter("email", email);
    Account account = query.uniqueResult();
    account.setPassword(password);
    session.merge(account);
    session.getTransaction().commit();
  }

  public Account login(String email, String password) {
    session.beginTransaction();
    Query<Account> query = session.createQuery("FROM Account WHERE email = :email", Account.class);
    query.setParameter("email", email);
    Account acc = query.uniqueResult();
    session.getTransaction().commit();

    if (acc == null) {
      return null;
    }

    if (acc.getPassword().equals(password)) {
      return acc;
    } else {
      return null;
    }
  }

  public void createUserAccount(String name, String email, String password, String location,
      SecurityQuestion securityQuestion, String answer, LocalDate birthDate, int contact) {
    session.beginTransaction();
    User user = new User(name, email, password, location, securityQuestion, answer, birthDate, contact);
    session.persist(user);
    session.getTransaction().commit();
  }

  public void createAdminAccount(String name, String email, String password, String location,
      SecurityQuestion securityQuestion, String answer) {
    session.beginTransaction();
    Account admin = new Account(name, email, password, location, securityQuestion, answer);
    session.persist(admin);
    session.getTransaction().commit();
  }

  public void createShelterAccount(String name, String email, String password, String location,
      SecurityQuestion securityQuestion, String answer, int foundationYear, int contact) {
    session.beginTransaction();
    Shelter shelter = new Shelter(name, email, password, location, securityQuestion, answer, foundationYear, contact);
    session.persist(shelter);
    session.getTransaction().commit();
  }

  public void setup() {
    final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
    try {
      sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
      session = sessionFactory.openSession();
    } catch (Exception e) {
      StandardServiceRegistryBuilder.destroy(registry);
      e.printStackTrace();
    }
  }

  public List<ShelterAnimal> getAvailableAnimalsByShelter(Shelter shelter) {
    session.beginTransaction();
    Query<ShelterAnimal> query = session.createQuery(
        "FROM ShelterAnimal WHERE shelter = :shelter AND listedFor != :status",
        ShelterAnimal.class);
    query.setParameter("shelter", shelter);
    query.setParameter("status", AdoptionType.NOT_AVAILABLE);
    List<ShelterAnimal> animals = query.getResultList();
    session.getTransaction().commit();
    return animals;
  }

  // Method to get the animals from a shelter
  public List<ShelterAnimal> getAnimalsByShelter(Shelter shelter) {
    session.beginTransaction();
    Query<ShelterAnimal> query = session.createQuery("FROM ShelterAnimal WHERE shelter = :shelter",
        ShelterAnimal.class);
    query.setParameter("shelter", shelter);
    List<ShelterAnimal> animals = query.getResultList();
    session.getTransaction().commit();
    return animals;
  }

  // Method to register animals as a Shelter
  public void registerAnimal(Shelter shelter, String name, AnimalType type, String race, AnimalSize size, AnimalGender gender,
                             int age, AnimalColor color, String description, AdoptionType adoptionType) {
    session.beginTransaction();
    ShelterAnimal animal = new ShelterAnimal(name, type, race, color, false, size, gender, adoptionType, description, shelter);
    session.persist(animal);
    session.getTransaction().commit();
  }

  public void exit() {
    session.close();
    sessionFactory.close();
  }

  public boolean validatePassword(String password) {
    PasswordValidator validator = new PasswordValidator(Arrays.asList(new LengthRule(8, 16),
        new CharacterRule(EnglishCharacterData.UpperCase, 1), new CharacterRule(EnglishCharacterData.Digit, 1),
        new CharacterRule(EnglishCharacterData.Special, 1), new WhitespaceRule()));
    RuleResult result = validator.validate(new PasswordData(password));
    return result.isValid();
  }

  public boolean validateEmail(String email) {
    EmailValidator validator = EmailValidator.getInstance();
    if (!validator.isValid(email)) {
      return false;
    } else {
      return true;
    }
  }

  public boolean doesEmailExist(String email) {
    Query<Account> query = session.createQuery("FROM Account WHERE email = :email", Account.class);
    query.setParameter("email", email);
    Account acc = query.uniqueResult();
    if (acc == null) {
      return false;
    } else {
      return true;
    }
  }

  public boolean validateFields(String... strings) {
    for (String string : strings) {
      if (string == null || string.isBlank()) {
        return false;
      }
    }
    return true;
  }

  public void showLostAnimals() {
    Query<LostAnimal> query = session.createQuery("FROM LostAnimal", LostAnimal.class);
    List<LostAnimal> lostAnimals = query.getResultList();
    for (LostAnimal animal : lostAnimals) {
      System.out.println(animal);
    }
  }

  public void showMyLostAnimals(Account user) {
    Query<LostAnimal> query = session.createQuery("FROM LostAnimal WHERE account =:user", LostAnimal.class);
    query.setParameter("user", user);
    List<LostAnimal> lostAnimals = query.getResultList();
    for (LostAnimal animal : lostAnimals) {
      System.out.println(animal);
    }
  }

  /**
   * Marks a user's lost animal as found.
   */
  public void foundMyAnimal(Account user) {
    session.beginTransaction();

    Query<LostAnimal> query = session.createQuery("FROM LostAnimal WHERE account = :user", LostAnimal.class);
    query.setParameter("user", user);
    List<LostAnimal> lostAnimals = query.getResultList();

    if (lostAnimals.isEmpty()) {
      System.out.println("You have no registered lost animals.");
      session.getTransaction().rollback();
      return;
    }

    System.out.println("Choose found animal (or -1 to cancel):");
    for (int i = 0; i < lostAnimals.size(); i++) {
      System.out.println(i + ": " + lostAnimals.get(i));
    }

    Scanner scanner = new Scanner(System.in);
    int choice;

    try {
      choice = scanner.nextInt();
      scanner.nextLine();
    } catch (Exception e) {
      System.out.println("Invalid input.");
      session.getTransaction().rollback();
      foundMyAnimal(user);
      return;
    }

    if (choice == -1) {
      System.out.println("Cancelled. Returning...");
      session.getTransaction().rollback();
      return;
    }

    if (choice < 0 || choice >= lostAnimals.size()) {
      System.out.println("Invalid index.");
      session.getTransaction().rollback();
      foundMyAnimal(user);
      return;
    }

    LostAnimal foundAnimal = lostAnimals.get(choice);
    System.out.println("Congratulations on finding your animal!");

    // 🟢 Safe remove — make sure it's attached to the session
    session.remove(foundAnimal);
    session.getTransaction().commit();
  }

  /**
   * Registers a new lost animal.
   */
  public void registerLostAnimal(User user, String name, AnimalType type, String race, AnimalColor color, AnimalSize size,
                                 AnimalGender gender, String description, int contact, String location) {
          session.beginTransaction();
          LostAnimal animal = new  LostAnimal(name, type, race, color, size, gender, description, contact, location);
          session.persist(animal);
          session.getTransaction().commit();

  }
}
