package iti.gov.producer.repository;

import iti.gov.producer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    List<User> findByLastname(String lastname);

    @Query("SELECT u FROM User u WHERE u.firstname = :firstname")
    List<User> findUsersByFirstName(@Param("firstname") String firstname);

    boolean existsByUsername(String username);
}