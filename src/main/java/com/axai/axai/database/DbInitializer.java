package com.axai.axai.database;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DbInitializer {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.database-name}")
    private String dbName;

    @PostConstruct
    public void init() {
        DbCreator.createDatabaseIfNotExists(dbUrl, username, password, dbName);
    }
}
