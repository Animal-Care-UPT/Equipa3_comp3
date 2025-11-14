// package AnimalCareCentre.client;
//
// import java.time.LocalDate;
// import java.util.List;
// import java.util.Scanner;
//
// import org.apache.commons.validator.routines.EmailValidator;
// import org.hibernate.Session;
// import org.hibernate.SessionFactory;
// import org.hibernate.boot.MetadataSources;
// import org.hibernate.boot.registry.StandardServiceRegistry;
// import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
// import org.hibernate.query.Query;
//
// import AnimalCareCentre.enums.*;
// import AnimalCareCentre.models.*;
// import AnimalCareCentre.utils.ACCPasswordValidator;
//
// public class ACCManager {
//
//   public ACCManager() {
//     setup();
//   }
//
//   private static SessionFactory sessionFactory;
//   private static Session session;
//
//   public void adoptAnimal(User user, ShelterAnimal animal, AdoptionType type) {
//     session.beginTransaction();
//     Adoption adopt = new Adoption(user, animal, type);
//     session.persist(adopt);
//     session.getTransaction().commit();
//   }
//
//   public void createSponsorship(User user, ShelterAnimal animal, float amount) {
//     session.beginTransaction();
//     Sponsorship sponsor = new Sponsorship(user, animal, amount);
//     session.persist(sponsor);
//     session.getTransaction().commit();
//   }
//
//   public List<Adoption> getUserAdoptions(User user) {
//     Query<Adoption> query = session.createQuery("FROM Adoption WHERE user = :user", Adoption.class);
//     query.setParameter("user", user);
//     return query.getResultList();
//   }
//
//   public List<ShelterAnimal> viewAllAnimals() {
//     Query<ShelterAnimal> query = session.createQuery("FROM ShelterAnimal", ShelterAnimal.class);
//     return query.getResultList();
//   }
//
//   public List<Adoption> viewAllAdoptions(AdoptionType type) {
//     Query<Adoption> query = session.createQuery("FROM Adoption WHERE type = :type", Adoption.class);
//     query.setParameter("type", type);
//     return query.getResultList();
//   }
//
//   /**
//    * This method searches animals by a keyword
//    *
//    * @param search
//    * @return
//    */
//   public List<ShelterAnimal> searchAnimalByKeyword(String search) {
//     Query<ShelterAnimal> query = session.createQuery(
//         "FROM ShelterAnimal WHERE listedFor != :status AND (" +
//             "name LIKE :search OR race LIKE :search OR " +
//             "CAST(type AS string) LIKE :search OR " +
//             "CAST(size AS string) LIKE :search OR " +
//             "CAST(color AS string) LIKE :search)",
//         ShelterAnimal.class);
//     query.setParameter("search", "%" + search + "%");
//     query.setParameter("status", AdoptionType.NOT_AVAILABLE);
//     return query.getResultList();
//   }
//
//   public List<Sponsorship> viewAllSponsorships() {
//     Query<Sponsorship> query = session.createQuery("FROM Sponsorship", Sponsorship.class);
//     return query.getResultList();
//   }
//
//   public List<Shelter> searchShelters() {
//     Query<Shelter> query = session.createQuery("FROM Shelter WHERE status = :status", Shelter.class);
//     query.setParameter("status", Status.ACCEPTED);
//     return query.getResultList();
//   }
//
//   /**
//    * This method searches animals by parameter
//    *
//    * @param parameter
//    * @param search
//    * @return
//    */
//   public List<ShelterAnimal> searchAnimalByParameter(String parameter, Object search) {
//     Query<ShelterAnimal> query = session.createQuery(
//         "From ShelterAnimal WHERE " + parameter + " =:search AND listedFor != :status", ShelterAnimal.class);
//     query.setParameter("search", search);
//     query.setParameter("status", AdoptionType.NOT_AVAILABLE);
//     return query.getResultList();
//   }
//
//   public void changePassword(String email, String password) {
//     String encryptedPw = encript(password);
//     session.beginTransaction();
//     Query<Account> query = session.createQuery("FROM Account WHERE email =:email", Account.class);
//     query.setParameter("email", email);
//     Account account = query.uniqueResult();
//     account.setPassword(encryptedPw);
//     session.merge(account);
//     session.getTransaction().commit();
//   }
//
//   public Account login(String email, String password) {
//     session.beginTransaction();
//     Query<Account> query = session.createQuery("FROM Account WHERE email = :email", Account.class);
//     query.setParameter("email", email);
//     Account acc = query.uniqueResult();
//     session.getTransaction().commit();
//
//     if (acc == null) {
//       return null;
//     }
//
//     String pw = decrypt(acc.getPassword());
//
//     if (pw.equals(password)) {
//       return acc;
//     } else {
//       return null;
//     }
//   }
//
//   public List<Shelter> showShelterRequests() {
//     Query<Shelter> query = session.createQuery("FROM Shelter WHERE status = :status", Shelter.class);
//     query.setParameter("status", Status.PENDING);
//     return query.getResultList();
//   }
//   
//   public void changeShelterStatus(Shelter shelter, Status status) {
//     session.beginTransaction();
//     shelter.setStatus(status);
//     session.merge(shelter);
//     session.getTransaction().commit();
//   }
//
//   public void createUserAccount(String name, String email, String password, String location,
//       SecurityQuestion securityQuestion, String answer, LocalDate birthDate, int contact) {
//     String encryptedPw = encript(password);
//     String encryptedAnswer = encript(answer);
//     session.beginTransaction();
//     User user = new User(name, email, encryptedPw, location, securityQuestion, encryptedAnswer, birthDate, contact);
//     session.persist(user);
//     session.getTransaction().commit();
//   }
//
//   public void createAdminAccount(String name, String email, String password, String location,
//       SecurityQuestion securityQuestion, String answer) {
//     String encryptedPw = encript(password);
//     String encryptedAnswer = encript(answer);
//     session.beginTransaction();
//     Account admin = new Account(name, email, encryptedPw, location, securityQuestion, encryptedAnswer);
//     session.persist(admin);
//     session.getTransaction().commit();
//   }
//
//   public void createShelterAccount(String name, String email, String password, String location,
//       SecurityQuestion securityQuestion, String answer, int foundationYear, int contact) {
//     String encryptedPw = encript(password);
//     String encryptedAnswer = encript(answer);
//     session.beginTransaction();
//     Shelter shelter = new Shelter(name, email, encryptedPw, location, securityQuestion, encryptedAnswer, foundationYear,
//         contact);
//     session.persist(shelter);
//     session.getTransaction().commit();
//   }
//
//   public void setup() {
//     final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
//     try {
//       sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
//       session = sessionFactory.openSession();
//     } catch (Exception e) {
//       StandardServiceRegistryBuilder.destroy(registry);
//       e.printStackTrace();
//     }
//   }
//
//   /**
//    * This method returns the adoptions by user
//    *
//    * @param user 
//    * @param type 
//    * @return 
//    */
//   public List<Adoption> getAdoptionsByUser(User user, AdoptionType type) {
//     session.beginTransaction();
//     Query<Adoption> query = session.createQuery("FROM Adoption WHERE user = :user AND type = :type", Adoption.class);
//     query.setParameter("user", user);
//     query.setParameter("type", type);
//     List<Adoption> adoptions = query.getResultList();
//     session.getTransaction().commit();
//     return adoptions;
//   }
//
//   /**
//    * This method returns the avaiable animals from a shelter
//    *
//    * @param shelter 
//    * @return 
//    */
//   public List<ShelterAnimal> getAvailableAnimalsByShelter(Shelter shelter) {
//     session.beginTransaction();
//     Query<ShelterAnimal> query = session.createQuery(
//         "FROM ShelterAnimal WHERE shelter = :shelter AND listedFor != :status",
//         ShelterAnimal.class);
//     query.setParameter("shelter", shelter);
//     query.setParameter("status", AdoptionType.NOT_AVAILABLE);
//     List<ShelterAnimal> animals = query.getResultList();
//     session.getTransaction().commit();
//     return animals;
//   }
//
//   /**
//    * This method returns the animals of a shelter
//    *
//    * @param shelter 
//    * @return 
//    */
//   public List<ShelterAnimal> getAnimalsByShelter(Shelter shelter) {
//     session.beginTransaction();
//     Query<ShelterAnimal> query = session.createQuery("FROM ShelterAnimal WHERE shelter = :shelter",
//         ShelterAnimal.class);
//     query.setParameter("shelter", shelter);
//     List<ShelterAnimal> animals = query.getResultList();
//     session.getTransaction().commit();
//     return animals;
//   }
//
//   /**
//    * This method registers animals on a shelter
//    *
//    * @param shelter 
//    * @param name 
//    * @param type 
//    * @param race 
//    * @param size 
//    * @param gender 
//    * @param age 
//    * @param color 
//    * @param description 
//    * @param adoptionType 
//    */
//   public void registerAnimal(Shelter shelter, String name, AnimalType type, String race, AnimalSize size,
//       AnimalGender gender,
//       int age, AnimalColor color, String description, AdoptionType adoptionType) {
//     session.beginTransaction();
//     ShelterAnimal animal = new ShelterAnimal(name, type, race, color, false, size, gender, adoptionType, description,
//         shelter);
//     session.persist(animal);
//     session.getTransaction().commit();
//   }
//
//   public void showLostAnimals() {
//     Query<LostAnimal> query = session.createQuery("FROM LostAnimal", LostAnimal.class);
//     List<LostAnimal> lostAnimals = query.getResultList();
//     for (LostAnimal animal : lostAnimals) {
//       System.out.println(animal);
//     }
//   }
//
//   public void showMyLostAnimals(Account user) {
//     Query<LostAnimal> query = session.createQuery("FROM LostAnimal WHERE account =:user", LostAnimal.class);
//     query.setParameter("user", user);
//     List<LostAnimal> lostAnimals = query.getResultList();
//     for (LostAnimal animal : lostAnimals) {
//       System.out.println(animal);
//     }
//   }
//
//   /**
//    * Marks a user's lost animal as found.
//    */
//   public void foundMyAnimal(Account user) {
//     session.beginTransaction();
//
//     Query<LostAnimal> query = session.createQuery("FROM LostAnimal WHERE account = :user", LostAnimal.class);
//     query.setParameter("user", user);
//     List<LostAnimal> lostAnimals = query.getResultList();
//
//     if (lostAnimals.isEmpty()) {
//       System.out.println("You have no registered lost animals.");
//       session.getTransaction().rollback();
//       return;
//     }
//
//     System.out.println("Choose found animal (or -1 to cancel):");
//     for (int i = 0; i < lostAnimals.size(); i++) {
//       System.out.println(i + ": " + lostAnimals.get(i));
//     }
//
//     Scanner scanner = new Scanner(System.in);
//     int choice;
//
//     try {
//       choice = scanner.nextInt();
//       scanner.nextLine();
//     } catch (Exception e) {
//       System.out.println("Invalid input.");
//       session.getTransaction().rollback();
//       foundMyAnimal(user);
//       return;
//     }
//
//     if (choice == -1) {
//       System.out.println("Cancelled. Returning...");
//       session.getTransaction().rollback();
//       return;
//     }
//
//     if (choice < 0 || choice >= lostAnimals.size()) {
//       System.out.println("Invalid index.");
//       session.getTransaction().rollback();
//       foundMyAnimal(user);
//       return;
//     }
//
//     LostAnimal foundAnimal = lostAnimals.get(choice);
//     System.out.println("Congratulations on finding your animal!");
//
//     session.remove(foundAnimal);
//     session.getTransaction().commit();
//   }
//
//   /**
//    * Registers a new lost animal.
//    */
//   public void registerLostAnimal(User user, String name, AnimalType type, String race, AnimalColor color,
//       AnimalSize size,
//       AnimalGender gender, String description, int contact, String location) {
//     session.beginTransaction();
//     LostAnimal animal = new LostAnimal(name, type, race, color, size, gender, description, contact, location);
//     session.persist(animal);
//     session.getTransaction().commit();
//
//   }
//
//   public void exit() {
//     session.close();
//     sessionFactory.close();
//   }
//
//   public String validatePassword(String password) {
//     ACCPasswordValidator validator = new ACCPasswordValidator();
//     return validator.validate(password);
//   }
//
//   public boolean validateEmail(String email) {
//     EmailValidator validator = EmailValidator.getInstance();
//     if (!validator.isValid(email)) {
//       return false;
//     } else {
//       return true;
//     }
//   }
//
//   public boolean doesEmailExist(String email) {
//     Query<Account> query = session.createQuery("FROM Account WHERE email = :email", Account.class);
//     query.setParameter("email", email);
//     Account acc = query.uniqueResult();
//     if (acc == null) {
//       return false;
//     } else {
//       return true;
//     }
//   }
//
//   public boolean validateFields(String... strings) {
//     for (String string : strings) {
//       if (string == null || string.isBlank()) {
//         return false;
//       }
//     }
//     return true;
//   }
//
//   public String encript(String text) {
//     String abc = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!#@_$-";
//     StringBuilder result = new StringBuilder(text.length());
//     String key = "lAsAgNa";
//     int k = 0;
//     int sum;
//
//     for (int i = 0; i < text.length(); i++) {
//       int textValue = abc.indexOf(text.charAt(i));
//       int keyValue = abc.indexOf(key.charAt(k));
//       sum = textValue + keyValue + 1;
//
//       if (sum >= abc.length()) {
//         sum -= abc.length();
//       }
//
//       k++;
//
//       if (k >= key.length()) {
//         k = 0;
//       }
//
//       result.append(abc.charAt(sum));
//     }
//
//     return result.toString();
//   }
//
//   public String decrypt(String text) {
//     String abc = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!#@_$-";
//     String key = "lAsAgNa";
//     StringBuilder result = new StringBuilder();
//     int k = 0;
//
//     for (int i = 0; i < text.length(); i++) {
//       int textValue = abc.indexOf(text.charAt(i));
//       int keyValue = abc.indexOf(key.charAt(k));
//
//       int sum = textValue - keyValue - 1;
//       if (sum < 0)
//         sum += abc.length();
//
//       result.append(abc.charAt(sum));
//       k++;
//
//       if (k >= key.length())
//         k = 0;
//     }
//
//     return result.toString();
//   }
//
//     /**
//      * Returns pending adoption/foster requests for a specific shelter
//      *
//      * @param shelter
//      * @param type
//      */
//     public List<Adoption> getPendingRequestsByShelter(Shelter shelter, AdoptionType type) {
//         session.beginTransaction();
//         Query<Adoption> query = session.createQuery(
//                 "FROM Adoption WHERE animal.shelter = :shelter AND type = :type AND status = :status",
//                 Adoption.class);
//         query.setParameter("shelter", shelter);
//         query.setParameter("type", type);
//         query.setParameter("status", Status.PENDING); // Assumes you have this enum
//         List<Adoption> adoptions = query.getResultList();
//         session.getTransaction().commit();
//         return adoptions;
//     }
//
//     /**
//      * Changes the status of an adoption request
//      *
//      * @param adoption
//      * @param status
//      */
//     public void changeAdoptionStatus(Adoption adoption, Status status) {
//         session.beginTransaction();
//         adoption.setStatus(status);
//         if (status == Status.ACCEPTED) {
//             // Mark animal as not available
//             adoption.getAnimal().setListedFor(AdoptionType.NOT_AVAILABLE);
//             session.merge(adoption.getAnimal());
//         }
//         session.merge(adoption);
//         session.getTransaction().commit();
//     }
//
//     /**
//      * @param shelter
//      * @param type
//      */
//     public List<Adoption> getCompletedAdoptionsByShelter(Shelter shelter, AdoptionType type) {
//         session.beginTransaction();
//         Query<Adoption> query = session.createQuery(
//                 "FROM Adoption WHERE animal.shelter = :shelter AND type = :type AND status = :status",
//                 Adoption.class);
//         query.setParameter("shelter", shelter);
//         query.setParameter("type", type);
//         query.setParameter("status", Status.ACCEPTED);
//         List<Adoption> adoptions = query.getResultList();
//         session.getTransaction().commit();
//         return adoptions;
//     }
//
//
// }
