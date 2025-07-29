package com.axai.axai.service;

import com.axai.axai.entities.App;
import com.axai.axai.entities.Menu;
import com.axai.axai.entities.SubMenu;
import com.axai.axai.repository.AppRepository;
import com.axai.axai.repository.MenuRepository;
import com.axai.axai.repository.SubMenuRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MenuService {
    private final SubMenuRepository subMenuRepository;
    private final MenuRepository menuRepository;
    private final AppRepository  appRepository;

    public SubMenu createSubMenu(UUID menuId, String subMenuName){
        Menu menu =  menuRepository.findById(menuId).orElseThrow(() -> new RuntimeException("Menu not found"));
        SubMenu subMenu = new SubMenu();
        subMenu.setName(subMenuName);
        subMenu.setMenu(menu);

        return subMenuRepository.save(subMenu);
    }

    public SubMenu updateSubMenuName(UUID subMenuId, String newName){
        SubMenu subMenu = subMenuRepository.findById(subMenuId)
                .orElseThrow(() -> new RuntimeException("SubMenu not found"));

        subMenu.setName(newName);
        return subMenuRepository.save(subMenu);
    }

    public SubMenu addAppsToSubMenu(UUID subMenuId, List<UUID> appIds) {
        SubMenu subMenu = subMenuRepository.findById(subMenuId)
                .orElseThrow(() -> new RuntimeException("SubMenu not found"));

        List<App> appsToAdd = appRepository.findAllById(appIds);
        subMenu.getApps().addAll(appsToAdd);

        return subMenuRepository.save(subMenu);
    }

    public SubMenu removeAppFromSubMenu(UUID subMenuId, UUID appId) {
        SubMenu subMenu = subMenuRepository.findById(subMenuId)
                .orElseThrow(() -> new RuntimeException("SubMenu not found"));

        subMenu.getApps().removeIf(app -> app.getId().equals(appId));
        return subMenuRepository.save(subMenu);
    }

    public void deleteSubMenu(UUID subMenuId) {
        if (!subMenuRepository.existsById(subMenuId)) {
            throw new RuntimeException("SubMenu not found");
        }
        subMenuRepository.deleteById(subMenuId);
    }

    @Transactional
    public Menu getFullMenuByUserId(UUID userId) {
        Menu menu = menuRepository.findMenuWithSubMenusByUserId(userId);

        menu.getSubMenus().forEach(subMenu -> {
            subMenu.getApps().size();
        });

        return menu;
    }
}
