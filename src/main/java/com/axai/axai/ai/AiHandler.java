package com.axai.axai.ai;


import com.axai.axai.entities.*;
import com.axai.axai.service.AppService;
import com.axai.axai.service.MenuService;
import com.axai.axai.service.ThemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AiHandler {

    private final AppService appService;
    private final MenuService menuService;
    private final ThemeService themeService;

    User loggedinUser;

    public void askAi(String input, User user){
        loggedinUser = user;
        String[] command = input.split(":");

        switch(command[0]){
            case "menu":
                listMenu();
                break;
            case "create menu":
                createSubMenu(command);
                break;
            case "rename menu":
                renameSubMenu(command);
                break;
            case "delete menu":
                deleteSubMenu(command);
                break;
            case "add app":
                addAppToSubMenu(command);
                break;
            case "remove app menu":
                removeAppFromSubMenu(command);
                break;
            case "download app":
                createApp(command);
                break;
            case "delete app":
                deleteApp(command);
                break;
            case "update app icon":
                updateAppIcon(command);
                break;
            case "delete app icon":
                deleteAppIcon(command);
                break;
            case "run app":
                runApp(command);
                break;
            case "set theme":
                setTheme(command);
                break;
            case "add theme":
                addTheme(command);
                break;
            default:
                System.out.println("Invalid input. Please try again.");
                break;

    }
}

    private void addTheme(String[] command) {
        String themeName = command[1];

        try{
            Theme theme = themeService.addTheme(themeName);
            System.out.println("Theme: " + theme.getName()+" created successfully.");
        } catch (RuntimeException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void setTheme(String[] command) {
        String themeName = command[1];

        try{
            themeService.setTheme(loggedinUser.getUsername(), themeName);
            System.out.println("Theme: "+themeName+" set successful.");
        } catch (RuntimeException e) {
            System.out.println("Error: "+e.getMessage());
        }
    }

    private void runApp(String[] command) {
        String appName = command[1];

        try {
            appService.runApp(appName,loggedinUser);
        }catch (RuntimeException e){
            System.out.println("Error: "+e.getMessage());
        }
    }

    private void deleteAppIcon(String[] command) {
        String appName = command[1];

        try{
            App app = appService.deleteIcon(appName,loggedinUser);
            System.out.println(app.getName()+"'s icon deleted successfully.");
        }catch (RuntimeException e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void updateAppIcon(String[] command) {
        String appName = command[1];

        String newIcon = command[2];

        try {
            App app = appService.updateIcon(appName, newIcon,loggedinUser);
            System.out.println("Icon updated successfully for app '" + app.getName() + "'.");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteApp(String[] command) {
        String appName = command[1];

        try {
            appService.deleteApp(appName,loggedinUser);
            System.out.println("App '" + appName + "' deleted successfully.");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void createApp(String[] command) {
        String name = command[1];

        String iconName = command[2];

        try {
            App app = appService.addApp(name, iconName.isBlank() ? null : iconName,loggedinUser);
            System.out.println("App '" + app.getName() + "' added successfully.");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void removeAppFromSubMenu(String[] command) {
        String subMenuName = command[1];

        String appName = command[2];

        try {
            SubMenu updated = menuService.removeAppFromSubMenu(
                    loggedinUser.getMenu().getId(),
                    subMenuName,
                    appName
            );
            System.out.println("App '" + appName + "' removed from submenu '" + updated.getName() + "'.");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void addAppToSubMenu(String[] command) {
        String subMenuName = command[1];
        String appName = command[2];

        try {
            SubMenu updated = menuService.addAppToSubMenu(
                    loggedinUser.getMenu().getId(),
                    subMenuName,
                    appName,
                    loggedinUser
            );
            System.out.println("Apps added to submenu '" + updated.getName() + "'.");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteSubMenu(String[] command) {
        String subMenuName = command[1];

        try {
            UUID menuId = loggedinUser.getMenu().getId();
            menuService.deleteSubMenu(menuId, subMenuName);
            System.out.println("Submenu '" + subMenuName + "' deleted.");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void renameSubMenu(String[] command) {
        String oldName = command[1];
        String newName = command[2];

        try {
            UUID menuId = loggedinUser.getMenu().getId();
            SubMenu updated = menuService.updateSubMenuName(menuId, oldName, newName);
            System.out.println("Submenu renamed to: " + updated.getName());
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void createSubMenu(String[] command) {
        String name = command[1];

        try {
            UUID menuId = loggedinUser.getMenu().getId();
            SubMenu created = menuService.createSubMenu(menuId, name);

            System.out.println("Submenu created: " + created.getName());
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    public void listMenu(){
        try {
            Menu menu = menuService.getFullMenuByUserId(loggedinUser.getId());

            if (menu == null) {
                System.out.println("No menu found.");
                return;
            }

            System.out.println(menu.getName());

            for (SubMenu subMenu : menu.getSubMenus()) {
                System.out.println("  - " + subMenu.getName());

                if (subMenu.getApps().isEmpty()) {
                    System.out.println("    (No apps)");
                    continue;
                }

                for (App app : subMenu.getApps()) {
                    System.out.println("    - " + app.getName());
                }
            }
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}
