package com.axai.axai.cli;

import com.axai.axai.entities.App;
import com.axai.axai.entities.Menu;
import com.axai.axai.entities.SubMenu;
import com.axai.axai.entities.User;
import com.axai.axai.service.AppService;
import com.axai.axai.service.MenuService;
import com.axai.axai.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Scanner;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CliHandler {

    private final UserService userService;
    private final MenuService menuService;
    private final AppService appService;

    Scanner scanner = new Scanner(System.in);
    User loggedinUser;
    String usName = "";

    public void start(){
        System.out.println("\nWelcome! Log in or create a user. Use help command for help.\n");

        boolean running = true;

        while(running){
            System.out.print("\n"+usName+"> ");
            String line = scanner.nextLine().trim();

            if(loggedinUser == null) {
                switch (line) {
                    case "help":
                        System.out.println(
                                """
                                        Create a user: create user
                                        Login: login
                                        Exit: exit
                                        """
                        );
                        break;
                    case "create user":
                        createUser();
                        break;
                    case "login":
                        login();
                        break;
                    case "exit":
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid input. Please try again.");
                        break;
                }
            }else{
                switch(line){
                    case "logout":
                        loggedinUser = null;
                        usName = "";
                        System.out.println("Logout successful.");
                        break;
                    case "rename user":
                        renameUser();
                        break;
                    case "change password":
                        changePassword();
                        break;
                    case "delete user":
                        deleteUser();
                        break;
                    case "menu":
                        listMenu();
                        break;
                    case "create menu":
                        createSubMenu();
                        break;
                    case "rename menu":
                        renameSubMenu();
                        break;
                    case "delete menu":
                        deleteSubMenu();
                        break;
                    case "add app":
                        addAppToSubMenu();
                        break;
                    case "remove app menu":
                        removeAppFromSubMenu();
                        break;
                    case "download app":
                        createApp();
                        break;
                    case "delete app":
                        deleteApp();
                        break;
                    case "update app icon":
                        updateAppIcon();
                        break;
                    case "delete app icon":
                        deleteAppIcon();
                        break;
                    case "run app":
                        runApp();
                        break;
                    case "help":
                        System.out.println(
                                """
                                        Logout: logout
                                        Exit: exit
                                        Edit your username: rename user
                                        Change your password: change password
                                        Delete your user: delete user
                                        List your menu:menu
                                        Create new submenu: create menu
                                        Rename submenu: rename menu
                                        Delete submenu: delete menu
                                        Add app to submenu: add app
                                        Remove app from submenu: remove app menu
                                        Create app: download app
                                        Delete app: delete app
                                        Update app icon: update app icon
                                        Delete app icon: delete app icon
                                        Run an app: run app
                                        """
                        );
                        break;
                    case "exit":
                        running = false;
                        loggedinUser = null;
                        break;
                    default:
                        System.out.println("Invalid input. Please try again.");
                        break;
                }
            }
        }
    }

    private void runApp(){
        System.out.println("Enter app name:");
        String appName = scanner.nextLine();

        try {
            appService.runApp(appName,loggedinUser);
        }catch (RuntimeException e){
            System.out.println("Error: "+e.getMessage());
        }
    }

    private void deleteAppIcon(){
        System.out.println("Enter app name:");
        String appName = scanner.nextLine();

        try{
            App app = appService.deleteIcon(appName,loggedinUser);
            System.out.println(app.getName()+"'s icon deleted successfully.");
        }catch (RuntimeException e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void updateAppIcon() {
        System.out.println("Enter app name:");
        String appName = scanner.nextLine();

        System.out.println("Enter new icon name:");
        String newIcon = scanner.nextLine();

        try {
            App app = appService.updateIcon(appName, newIcon,loggedinUser);
            System.out.println("Icon updated successfully for app '" + app.getName() + "'.");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteApp() {
        System.out.println("Enter app name to delete:");
        String appName = scanner.nextLine();

        try {
            appService.deleteApp(appName,loggedinUser);
            System.out.println("App '" + appName + "' deleted successfully.");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void createApp() {
        System.out.println("Enter app name:");
        String name = scanner.nextLine();

        System.out.println("Enter icon name (or leave blank):");
        String iconName = scanner.nextLine();

        try {
            App app = appService.addApp(name, iconName.isBlank() ? null : iconName,loggedinUser);
            System.out.println("App '" + app.getName() + "' added successfully.");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void removeAppFromSubMenu(){
        System.out.println("Enter submenu name:");
        String subMenuName = scanner.nextLine();

        System.out.println("Enter app name to remove:");
        String appName = scanner.nextLine();

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

    private void addAppToSubMenu() {
        System.out.println("Enter submenu name to add apps to:");
        String subMenuName = scanner.nextLine();

        System.out.println("Enter app name:");
        String appName = scanner.nextLine();

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


    private void deleteSubMenu() {
        System.out.println("Enter submenu name to delete:");
        String subMenuName = scanner.nextLine();

        try {
            UUID menuId = loggedinUser.getMenu().getId();
            menuService.deleteSubMenu(menuId, subMenuName);
            System.out.println("Submenu '" + subMenuName + "' deleted.");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void renameSubMenu() {
        System.out.println("Enter current submenu name:");
        String oldName = scanner.nextLine();
        System.out.println("Enter new submenu name:");
        String newName = scanner.nextLine();

        try {
            UUID menuId = loggedinUser.getMenu().getId();
            SubMenu updated = menuService.updateSubMenuName(menuId, oldName, newName);
            System.out.println("Submenu renamed to: " + updated.getName());
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void createSubMenu(){
        System.out.println("Enter submenu name:");
        String name = scanner.nextLine();

        try {
            UUID menuId = loggedinUser.getMenu().getId();
            SubMenu created = menuService.createSubMenu(menuId, name);

            System.out.println("Submenu created: " + created.getName());
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void createUser(){
        System.out.println("Enter username: ");
        String username = scanner.nextLine();

        System.out.println("Enter password: ");
        String password = scanner.nextLine();

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);


        try {
            User created = userService.createUser(username, password);

            System.out.println("User: " + created.getUsername() + " created.");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void renameUser(){
        System.out.println("Enter your new username: ");
        String newName = scanner.nextLine();
        System.out.println("Enter your password: ");
        String password = scanner.nextLine();

        try {
            if (userService.checkIfUserExists(loggedinUser.getUsername(), password)) {
                User user = userService.renameUser(loggedinUser.getId(), newName);
                System.out.println("Username updated to " + user.getUsername() + "successfully.");
            } else {
                System.out.println("Invalid password.");
            }
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    private void changePassword(){
        System.out.println("Enter your current password: ");
        String password = scanner.nextLine();

        System.out.println("Enter your new password: ");
        String newPassword = scanner.nextLine();

        try {
            if (userService.checkIfUserExists(loggedinUser.getUsername(), password)) {
                User user = userService.changePassword(loggedinUser.getId(), newPassword);
                System.out.println("User's: " + user.getUsername() + "Password updated successfully.");
            } else {
                System.out.println("Invalid password.");
            }
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    private void deleteUser(){
        System.out.println("Enter your password: ");
        String password = scanner.nextLine();

        try {
            if (userService.checkIfUserExists(loggedinUser.getUsername(), password)) {
                userService.deleteUser(loggedinUser.getId());
                loggedinUser = null;
                usName = "";
            } else {
                System.out.println("Invalid password.");
            }
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void listMenu(){
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

    private void login(){
        System.out.println("Enter username: ");
        String username = scanner.nextLine();

        System.out.println("Enter password: ");
        String password = scanner.nextLine();

        try {
            if (userService.checkIfUserExists(username, password)) {
                loggedinUser = userService.getUser(username);
                System.out.println("Login successful.");
                usName = username;
            } else {
                System.out.println("Login failed. Try again.");
            }
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
