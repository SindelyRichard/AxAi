package com.axai.axai.repository;

import com.axai.axai.entities.Menu;
import com.axai.axai.entities.SubMenu;
import com.axai.axai.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface MenuRepository extends JpaRepository<Menu, UUID> {
    @Query("SELECT DISTINCT m FROM Menu m JOIN FETCH m.subMenus WHERE m.user.id = :userId")
    Menu findMenuWithSubMenusByUserId(@Param("userId") UUID userId);




}
