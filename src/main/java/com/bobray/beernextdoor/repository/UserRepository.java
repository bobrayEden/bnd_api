package com.bobray.beernextdoor.repository;

import com.bobray.beernextdoor.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByNameUser(String nameUser);
    Optional<User> findByEmail(String email);
    Optional<User> findUserByApiKey(String apiKey);
}
