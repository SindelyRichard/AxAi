package com.axai.axai.database;

import com.axai.axai.entities.Background;
import com.axai.axai.entities.Theme;
import com.axai.axai.repository.BackgroundRepository;
import com.axai.axai.repository.ThemeRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Initializes the database during application startup.
 * Ensures the database exists and seeds required default data.
 */
@Component
@RequiredArgsConstructor
public class DbInitializer {

    // Injecting database properties from application.properties
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

    /**
     * Called automatically after the component is initialized.
     * - Ensures the database exists.
     * - Seeds a default Theme entity if it doesn't already exist.
     */
    @PostConstruct
    public void init() {
        // Creates the database if it doesn't exist
        DbCreator.createDatabaseIfNotExists(dbUrl, username, password, dbName);

        // Seed the "Default" theme if it is not already presentc
        if(!themeRepository.existsByName("Default")) {
            Theme theme = new Theme();
            theme.setName("Default");
            themeRepository.save(theme);
        }
    }

    /**
     * Static method to create the database from application.properties file.
     */
    public static void createDbFromProperties() {
        try {
            var props = new java.util.Properties();

            // Load application.properties
            try (var input = DbInitializer.class.getClassLoader().getResourceAsStream("application.properties")) {
                props.load(input);
            }

            String url = props.getProperty("spring.datasource.url");
            String username = props.getProperty("spring.datasource.username");
            String password = props.getProperty("spring.datasource.password");
            String dbName = props.getProperty("spring.datasource.database-name");

            if (url != null && dbName != null) {
                // Create the database
                DbCreator.createDatabaseIfNotExists(url, username, password, dbName);
            } else {
                System.err.println("Error creating database from properties");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
