package com.axai.axai.cli;

import com.axai.axai.entities.App;
import com.axai.axai.entities.Menu;
import com.axai.axai.entities.SubMenu;
import com.axai.axai.entities.User;
import com.axai.axai.service.MenuService;
import com.axai.axai.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class CliHandler {
    Scanner scanner = new Scanner(System.in);
    private final UserService userService;
    private final MenuService menuService;
    User loggedinUser;
    String usName = "";

    public void start(){
        System.out.println("Welcome! Log in or create a user. Use help command for help.");

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
                    case "help":
                        System.out.println(
                                """
                                        Logout: logout
                                        Exit: exit
                                        Edit your username: rename user
                                        Change your password: change password
                                        Delete your user: delete user
                                        List your menu:menu
                                        """
                        );
                        break;
                    case "exit":
                        running = false;
                        loggedinUser = null;
                        break;

                }
            }
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

        User created = userService.createUser(username,password);

        System.out.println("User: "+username + " created.");
    }

    private void renameUser(){
        System.out.println("Enter your new username: ");
        String newName = scanner.nextLine();
        System.out.println("Enter your password: ");
        String password = scanner.nextLine();

        if(userService.checkIfUserExists(loggedinUser.getUsername(),password)){
            User user = userService.renameUser(loggedinUser.getId(),newName);
            System.out.println("Username updated successfully.");
        }else{
            System.out.println("Invalid password.");
        }

    }

    private void changePassword(){
        System.out.println("Enter your current password: ");
        String password = scanner.nextLine();

        System.out.println("Enter your new password: ");
        String newPassword = scanner.nextLine();

        if(userService.checkIfUserExists(loggedinUser.getUsername(),password)){
            User user = userService.changePassword(loggedinUser.getId(),newPassword);
            System.out.println("Password updated successfully.");
        }else{
            System.out.println("Invalid password.");
        }

    }

    private void deleteUser(){
        System.out.println("Enter your password: ");
        String password = scanner.nextLine();

        if(userService.checkIfUserExists(loggedinUser.getUsername(),password)){
            userService.deleteUser(loggedinUser.getId());
        }else{
            System.out.println("Invalid password.");
        }
    }

    private void listMenu(){
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
    }

    private void login(){
        System.out.println("Enter username: ");
        String username = scanner.nextLine();

        System.out.println("Enter password: ");
        String password = scanner.nextLine();

        if(userService.checkIfUserExists(username,password)){
            loggedinUser = userService.getUser(username);
            System.out.println("Login successful.");
            usName=username;
        }else{
            System.out.println("Login failed.");
            login();
        }
    }
}
