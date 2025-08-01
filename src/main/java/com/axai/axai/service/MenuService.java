package com.axai.axai.service;

import com.axai.axai.entities.App;
import com.axai.axai.entities.Menu;
import com.axai.axai.entities.SubMenu;
import com.axai.axai.entities.User;
import com.axai.axai.repository.AppRepository;
import com.axai.axai.repository.MenuRepository;
import com.axai.axai.repository.SubMenuRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MenuService {
    private final SubMenuRepository subMenuRepository;
    private final MenuRepository menuRepository;
    private final AppRepository  appRepository;

    // Creates a new submenu
    public SubMenu createSubMenu(UUID menuId, String subMenuName){
        Menu menu =  menuRepository.findById(menuId).orElseThrow(() -> new RuntimeException("Menu not found"));
        if(subMenuRepository.existsByNameAndMenu_Id(subMenuName,menuId)){
            throw new RuntimeException("SubMenu already exists");
        }
        SubMenu subMenu = new SubMenu();
        subMenu.setName(subMenuName);
        subMenu.setMenu(menu);

        return subMenuRepository.save(subMenu);
    }

    // Updates the name of an existing submenu.
    public SubMenu updateSubMenuName(UUID menuId,String currentName, String newName){
        SubMenu subMenu = subMenuRepository.findSubMenuByNameAndMenuId(currentName,menuId);

        subMenu.setName(newName);
        return subMenuRepository.save(subMenu);
    }

    // Adds an app to a submenu.
    @Transactional
    public SubMenu addAppToSubMenu(UUID menuId, String subMenuName, String appName, User user) {
        SubMenu subMenu = subMenuRepository.findSubMenuByNameAndMenuId(subMenuName, menuId);
        App appToAdd = appRepository.findByNameAndUser(appName,user).orElseThrow(() -> new RuntimeException("App not found"));
        List<App> appsInSubMenu = subMenu.getApps();
        appsInSubMenu.add(appToAdd);
        return subMenuRepository.save(subMenu);
    }

    // Removes an app from submenu.
    @Transactional
    public SubMenu removeAppFromSubMenu(UUID menuId, String subMenuName, String appName) {
        SubMenu subMenu = subMenuRepository.findSubMenuByNameAndMenuId(subMenuName, menuId);
        subMenu.getApps().removeIf(app -> app.getName().equals(appName));
        return subMenuRepository.save(subMenu);
    }

    // Deletes a submenu from menu.
    public void deleteSubMenu(UUID menuId, String subMenuName) {
        SubMenu subMenu = subMenuRepository.findSubMenuByNameAndMenuId(subMenuName, menuId);
        subMenuRepository.delete(subMenu);
    }

    /**
     * Menu with all its SubMenus and their Apps by user ID.
     *
     * @param userId the ID of the User
     * @return the Menu entity with SubMenus and Apps
     */
    @Transactional
    public Menu getFullMenuByUserId(UUID userId) {
        Menu menu = menuRepository.findMenuWithSubMenusByUserId(userId);

        menu.getSubMenus().forEach(subMenu -> {
            subMenu.getApps().size();
        });

        return menu;
    }
}
