package com.axai.axai.repository;

import com.axai.axai.entities.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface MenuRepository extends JpaRepository<Menu, UUID> {
    Menu findMenuWithSubMenusByUserId(@Param("userId") UUID userId);
}
