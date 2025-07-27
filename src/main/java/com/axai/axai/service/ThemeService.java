package com.axai.axai.service;

import com.axai.axai.entities.Theme;
import com.axai.axai.entities.User;
import com.axai.axai.repository.ThemeRepository;
import com.axai.axai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ThemeService {
    private final ThemeRepository themeRepository;
    private final UserRepository  userRepository;

    public Theme addTheme(String themeName) {
        if (themeRepository.existsByName(themeName)) {
            throw new RuntimeException("Theme already exists");
        }
        Theme theme = new Theme();
        theme.setName(themeName);
        return themeRepository.save(theme);
    }

    public void setTheme(UUID userId,UUID themeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new RuntimeException("Theme not found"));

        user.setTheme(theme);
        userRepository.save(user);
    }
}
