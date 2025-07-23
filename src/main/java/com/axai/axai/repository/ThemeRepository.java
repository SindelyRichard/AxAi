package com.axai.axai.repository;

import com.axai.axai.entities.Theme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ThemeRepository extends JpaRepository<Theme, UUID> {
}
