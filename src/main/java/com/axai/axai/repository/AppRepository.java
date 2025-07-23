package com.axai.axai.repository;

import com.axai.axai.entities.App;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AppRepository extends JpaRepository<App, UUID> {
}
