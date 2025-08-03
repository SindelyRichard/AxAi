package com.axai.axai.service;

import com.axai.axai.entities.*;
import com.axai.axai.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final BackgroundRepository backgroundRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final ThemeRepository themeRepository;
    private final AppRepository appRepository;
    private final MenuRepository menuRepository;
    private final SubMenuRepository  subMenuRepository;

    /*
    Creates a new user with the given username and password.
    Also sets up default theme, background, menu, submenus, and initial apps.
    */
    public User createUser(String username, String password) {
        if(userRepository.existsByUsername(username)){
            throw new RuntimeException("Username already taken");
        }
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);

        Theme defaultTheme = themeRepository.findByName("Default")
                .orElseThrow(() -> new RuntimeException("Default theme not found"));
        user.setTheme(defaultTheme);

        User savedUser = userRepository.save(user);

        Background background = new Background();
        background.setUser(savedUser);
        background.setName("Default");
        backgroundRepository.save(background);

        savedUser.setSelectedBackground(background);

        Menu menu = new Menu();
        menu.setName("Main menu");
        menu.setUser(savedUser);

        SubMenu subMenu = new SubMenu();
        subMenu.setName("Apps");
        subMenu.setMenu(menu);

        App settingsApp = new App();
        settingsApp.setName("Settings");
        settingsApp.setIconName("settings_icon");
        settingsApp.setUser(savedUser);

        App notesApp = new App();
        notesApp.setName("Notes");
        notesApp.setIconName("notes_icon");
        notesApp.setUser(savedUser);

        List<App> apps = List.of(settingsApp, notesApp);
        settingsApp.setSubMenuList(List.of(subMenu));
        notesApp.setSubMenuList(List.of(subMenu));
        subMenu.setApps(apps);

        menu.setSubMenus(List.of(subMenu));
        savedUser.setMenu(menu);

        menuRepository.save(menu);
        appRepository.saveAll(apps);
        subMenuRepository.save(subMenu);

        return userRepository.save(savedUser);
    }

    // Renames an existing user identified by their UUID.
    public User renameUser(UUID id,String newUsername){
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        if (!user.getUsername().equals(newUsername) && userRepository.findByUsername(newUsername).isPresent()) {
            throw new RuntimeException("Username already taken");
        }

        user.setUsername(newUsername);
        return userRepository.save(user);
    }

    // Changes the password of an existing user.
    public User changePassword(UUID id,String newPassword){
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    //Deletes a user and all associated entities like menus, submenus, apps, and backgrounds.
    @Transactional
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getMenu() != null) {
            Menu menu = user.getMenu();

            for (SubMenu subMenu : menu.getSubMenus()) {
                subMenu.getApps().forEach(app -> {
                    app.getSubMenuList().remove(subMenu);
                });
                subMenuRepository.delete(subMenu);
            }

            menuRepository.delete(menu);
        }

        if (user.getSelectedBackground() != null) {
            backgroundRepository.delete(user.getSelectedBackground());
        }

        userRepository.delete(user);
    }

    // Checks if a user with the specified username exists and verifies the password.
    public boolean checkIfUserExists(String username,String password){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        return passwordEncoder.matches(password,user.getPassword());
    }
    // Retrieves a user entity by their username.
    public User getUser(String username){
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }
}