package com.axai.axai.cli;

import com.axai.axai.entities.User;
import com.axai.axai.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.Scanner;

@RequiredArgsConstructor
public class CliHandler {
    Scanner scanner = new Scanner(System.in);
    private final UserService userService;
    User loggedinUser;

    public void start(){
        System.out.println("Welcome! Log in or create a user. Use help command for help.");

        boolean running = true;

        while(running){
            System.out.print("\n> ");
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
                        System.out.println("Logout successful.");
                        break;
                    case "help":
                        System.out.println(
                                """
                                        Logout: logout
                                        Exit: exit
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

        userService.createUser(username,password);
        
        System.out.println("User: "+username + " created.");
    }

    private void login(){
        System.out.println("Enter username: ");
        String username = scanner.nextLine();

        System.out.println("Enter password: ");
        String password = scanner.nextLine();

        if(userService.checkIfUserExists(username,password)){
            loggedinUser = new User();
            loggedinUser.setUsername(username);
            loggedinUser.setPassword(password);
        }else{
            System.out.println("Login failed.");
            login();
        }
    }
}
