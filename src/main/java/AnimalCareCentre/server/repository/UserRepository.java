package AnimalCareCentre.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import AnimalCareCentre.server.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
