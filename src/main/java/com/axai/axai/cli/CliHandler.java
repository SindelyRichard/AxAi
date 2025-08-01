package com.axai.axai.cli;

import com.axai.axai.ai.Ai;
import com.axai.axai.entities.*;
import com.axai.axai.service.AppService;
import com.axai.axai.service.MenuService;
import com.axai.axai.service.ThemeService;
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
    private final ThemeService themeService;

    private final Ai ai;


    Scanner scanner = new Scanner(System.in);
    User loggedinUser;
    String usName = "";
    boolean running = false;


    public void start(){
        System.out.println("\nWelcome! Log in or create a user. Use help command for help.\n");

        running = true;

        while(running) {
            System.out.print("\n" + usName + "> ");
            String line = scanner.nextLine().trim();
            commandHandler(line);

        }
        scanner.close();
    }

    private void commandHandler(String input) {
        String[] command = input.split(" ");

            if(loggedinUser == null) {
                switch (command[0]) {
                    case "help":
                        System.out.println(
                                """
                                        Create a user: create-user userName password
                                        Login: login
                                        Exit: exit
                                        """
                        );
                        break;
                    case "create-user":
                        createUser(command);
                        break;
                    case "login":
                        login();
                        break;
                    case "exit":
                        ai.closeClient();
                        loggedinUser = null;
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid input. Please try again.");
                        break;
                }
            }else{
                    switch (command[0]) {
                        case "logout":
                            loggedinUser = null;
                            usName = "";
                            System.out.println("Logout successful.");
                            break;
                        case "rename-user":
                            renameUser(command);
                            break;
                        case "change-password":
                            changePassword(command);
                            break;
                        case "delete-user":
                            deleteUser(command);
                            break;
                        case "ai":
                            askAi();
                            break;
                        case "menu":
                            listMenu();
                            break;
                        case "create-menu":
                            createSubMenu(command);
                            break;
                        case "rename-menu":
                            renameSubMenu(command);
                            break;
                        case "delete-menu":
                            deleteSubMenu(command);
                            break;
                        case "add-app":
                            addAppToSubMenu(command);
                            break;
                        case "remove-app-menu":
                            removeAppFromSubMenu(command);
                            break;
                        case "download-app":
                            createApp(command);
                            break;
                        case "delete-app":
                            deleteApp(command);
                            break;
                        case "update-app-icon":
                            updateAppIcon(command);
                            break;
                        case "delete-app-icon":
                            deleteAppIcon(command);
                            break;
                        case "run-app":
                            runApp(command);
                            break;
                        case "set-theme":
                            setTheme(command);
                            break;
                        case "add-theme":
                            addTheme(command);
                            break;
                        case "simulation":
                            runSimulation();
                            break;
                        case "help":
                            System.out.println(
                                    """
                                            Logout: logout
                                            Exit: exit
                                            Edit your username: rename-user newName passwd
                                            Change your password: change-password password newPassword
                                            Delete your user: delete-user passwd
                                            List your menu:menu
                                            Create new submenu: create-menu menuName
                                            Rename submenu: rename-menu oldName NewName
                                            Delete submenu: delete-menu menuName
                                            Add app to submenu: add-app menuName appName
                                            Remove app from submenu: remove-app-menu menuName appName
                                            Create app: download-app appName iconName
                                            Delete app: delete-app appName
                                            Update app icon: update-app-icon appName newIconName
                                            Delete app icon: delete-app-icon appName
                                            Run an app: run-app appName
                                            Set your theme: set-theme themeName
                                            Add theme: add-theme themeName
                                            """
                            );
                            break;
                        case "exit":
                            ai.closeClient();
                            loggedinUser = null;
                            running = false;
                            break;
                        default:
                            System.out.println("Invalid input. Please try again.");
                            break;

                    }
        }
    }

    private void runSimulation() {
        String username = "demoUser";
        String password = "demoPass";

        try {
            try {
                userService.createUser(username, password);
            } catch (RuntimeException e) {
                System.out.println("User already exists.");
            }

            loggedinUser = userService.getUser(username);
            usName = username;

            themeService.addTheme("Dark");
            themeService.addTheme("Light");
            themeService.setTheme(username, "Dark");

            UUID menuId = loggedinUser.getMenu().getId();
            menuService.createSubMenu(menuId, "Development");
            menuService.createSubMenu(menuId, "Games");

            App app1 = appService.addApp("IntelliJ", "jetbrains", loggedinUser);
            App app2 = appService.addApp("Chess", "knight", loggedinUser);

            System.out.println("App '" + app1.getName() + "' added successfully.");
            System.out.println("App '" + app2.getName() + "' added successfully.");

            menuService.addAppToSubMenu(menuId, "Development", "IntelliJ", loggedinUser);
            menuService.addAppToSubMenu(menuId, "Games", "Chess", loggedinUser);

            System.out.println("Simulation created. Logged in as '" + username + "'.");
            listMenu();

        } catch (RuntimeException e) {
            System.out.println("Simulation failed: " + e.getMessage());
        }
    }

    private void askAi(){
        System.out.println("What do you need?");
        String input = scanner.nextLine();

        String response =  ai.askGPT("This is a command: "+input+". Tell me, what do I need to do:\n" +
                "                                        List your menu:menu\n" +
                "                                        Create new submenu: format is=create-menu menuName\n" +
                "                                        Rename submenu: format is=rename-menu oldSubMenuName newSubMenuName\n" +
                "                                        Delete submenu:format is=delete-menu subMenuName\n" +
                "                                        Add app to submenu:format is=add-app subMenuName appName\n" +
                "                                        Remove app from submenu: format is=remove-app-menu subMenuName appName\n" +
                "                                        Download app: format is=download-app appName iconName\n" +
                "                                        Delete app:format is=delete-app appName\n" +
                "                                        Update app icon: format is=update-app-icon appName newIconName\n" +
                "                                        Delete app icon:format is=delete-app-icon appName\n" +
                "                                        Run an app: format is=run-app appName\n" +
                "                                        Set your theme:format is=set-theme themeName\n" +
                "                                        Add theme:format is=add-theme themeName\n"+
                "                                        Run a simulation: format is=simulation\n"+
                "                                        ;  Give me the right command and parameter as a String.Format is: command parameter or command parameter1 parameter2.Example:download-app appName iconName.Put space between the parameters and Respond only with the command in the exact format, like: command param1 param2. DO NOT explain anything, DO NOT start the answer with anything else.");


        commandHandler(response);
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

    private void createUser(String[] command){
        String username = command[1];

        String password = command[2];

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

    private void renameUser(String[] command){
        String newName = command[1];
        String password = command[2];

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

    private void changePassword(String[] command){
        String password = command[1];

        String newPassword = command[2];

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

    private void deleteUser(String[] command){
        String password = command[1];

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
