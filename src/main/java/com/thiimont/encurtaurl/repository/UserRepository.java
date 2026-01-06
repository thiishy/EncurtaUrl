package com.thiimont.encurtaurl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.thiimont.encurtaurl.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUuid(UUID uuidUser);
    Optional<UserDetails> findUserByUsername(String username);
}
