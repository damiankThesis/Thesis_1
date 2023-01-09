package com.praca.komis.project.repository;

import com.praca.komis.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String email);
    Optional<User> findByUsernameAndPassword(String username, String password);

    boolean existsByUsername(String username);

    Optional<User> findByHash(String hash);

}
