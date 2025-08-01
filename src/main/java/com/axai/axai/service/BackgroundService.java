package com.axai.axai.service;

import com.axai.axai.entities.Background;
import com.axai.axai.entities.User;
import com.axai.axai.repository.BackgroundRepository;
import com.axai.axai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BackgroundService {
    private final BackgroundRepository backgroundRepository;
    private final UserRepository userRepository;

    // Adds a new background for a specific user.
    public Background addBackground(UUID userId, String backgroundName){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        if(backgroundRepository.existsByName(backgroundName)){
            throw new RuntimeException("Background already exists");
        }
        Background background = new Background();
        background.setName(backgroundName);
        background.setUser(user);

        return backgroundRepository.save(background);

    }

    // Sets the selected background for a specific user.
    public User selectBackground(UUID userId, String backgroundName){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Background background = backgroundRepository.findByNameAndUser(backgroundName,user).orElseThrow(() -> new RuntimeException("Background does not exists"));

        user.setSelectedBackground(background);

        return userRepository.save(user);
    }


}
