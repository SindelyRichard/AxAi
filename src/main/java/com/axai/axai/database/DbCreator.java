package com.axai.axai.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Class to create a MySQL database if it does not already exist.
 */
public class DbCreator {
    /**
     * Creates the database if it does not already exist.
     * Assumes that the URL contains the database name.
     *
     * @param url      the full JDBC URL including the database name
     * @param user     the database user
     * @param password the database password
     * @param dbName   the name of the database to create
     */
    public static void createDatabaseIfNotExists(String url,String user,String password,String dbName) {
        // Removes the database name from the URL to connect to the server.
        String urlWithoutDb = url.replace("/"+dbName,"");
        try(
                Connection connection = DriverManager.getConnection(urlWithoutDb,user,password);
                Statement statement = connection.createStatement()
        ){
            // SQL statement to create the database if it doesn't exist
            String sql = "CREATE DATABASE IF NOT EXISTS " + dbName;
            statement.executeUpdate(sql);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
