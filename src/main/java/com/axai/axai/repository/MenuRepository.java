package com.axai.axai.repository;

import com.axai.axai.entities.Menu;
import com.axai.axai.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MenuRepository extends JpaRepository<Menu, UUID> {

}
