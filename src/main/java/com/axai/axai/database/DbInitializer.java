package com.axai.axai.database;

import com.axai.axai.entities.Background;
import com.axai.axai.entities.Theme;
import com.axai.axai.repository.BackgroundRepository;
import com.axai.axai.repository.ThemeRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DbInitializer {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.database-name}")
    private String dbName;

    private final ThemeRepository  themeRepository;
    private final BackgroundRepository backgroundRepository;

    @PostConstruct
    public void init() {
        DbCreator.createDatabaseIfNotExists(dbUrl, username, password, dbName);

        if(!themeRepository.existsByName("Default")) {
            Theme theme = new Theme();
            theme.setName("Default");
            themeRepository.save(theme);
        }
    }
}
