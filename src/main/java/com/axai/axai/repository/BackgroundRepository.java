package com.axai.axai.repository;

import com.axai.axai.entities.Background;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BackgroundRepository extends JpaRepository<Background, UUID> {

}
