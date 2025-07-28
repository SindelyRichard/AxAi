package com.axai.axai.repository;

import com.axai.axai.entities.Background;
import com.axai.axai.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BackgroundRepository extends JpaRepository<Background, UUID> {
    boolean existsByName(String name);

    Optional<Background> findByNameAndUser(String name, User user);

    Optional<Background> findByName(String name);
}
