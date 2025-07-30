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

    public static void createDbFromProperties() {
        try {
            var props = new java.util.Properties();
            try (var input = DbInitializer.class.getClassLoader().getResourceAsStream("application.properties")) {
                props.load(input);
            }

            String url = props.getProperty("spring.datasource.url");
            String username = props.getProperty("spring.datasource.username");
            String password = props.getProperty("spring.datasource.password");
            String dbName = props.getProperty("spring.datasource.database-name");

            if (url != null && dbName != null) {
                DbCreator.createDatabaseIfNotExists(url, username, password, dbName);
            } else {
                System.err.println("Error creating database from properties");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
