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

    public SubMenu updateSubMenuName(UUID menuId,String currentName, String newName){
        SubMenu subMenu = subMenuRepository.findSubMenuByNameAndMenuId(currentName,menuId);

        subMenu.setName(newName);
        return subMenuRepository.save(subMenu);
    }

    @Transactional
    public SubMenu addAppToSubMenu(UUID menuId, String subMenuName, String appName, User user) {
        SubMenu subMenu = subMenuRepository.findSubMenuByNameAndMenuId(subMenuName, menuId);
        App appToAdd = appRepository.findByNameAndUser(appName,user).orElseThrow(() -> new RuntimeException("App not found"));
        List<App> appsInSubMenu = subMenu.getApps();
        appsInSubMenu.add(appToAdd);
        return subMenuRepository.save(subMenu);
    }

    @Transactional
    public SubMenu removeAppFromSubMenu(UUID menuId, String subMenuName, String appName) {
        SubMenu subMenu = subMenuRepository.findSubMenuByNameAndMenuId(subMenuName, menuId);
        subMenu.getApps().removeIf(app -> app.getName().equals(appName));
        return subMenuRepository.save(subMenu);
    }

    public void deleteSubMenu(UUID menuId, String subMenuName) {
        SubMenu subMenu = subMenuRepository.findSubMenuByNameAndMenuId(subMenuName, menuId);
        subMenuRepository.delete(subMenu);
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
