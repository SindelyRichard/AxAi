package com.axai.axai.repository;

import com.axai.axai.entities.SubMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SubMenuRepository extends JpaRepository<SubMenu, UUID> {
    SubMenu findSubMenuByNameAndMenuId(String name,UUID menuId);

    boolean existsByNameAndMenu_Id(String name, UUID menuId);
}
