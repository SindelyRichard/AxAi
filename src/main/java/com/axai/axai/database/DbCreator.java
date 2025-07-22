package com.axai.axai.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DbCreator {
    public static void createDatabaseIfNotExists(String url,String user,String password,String dbName) {
        String urlWithoutDb = url.replace("/"+dbName,"");
        try(
                Connection connection = DriverManager.getConnection(urlWithoutDb,user,password);
                Statement statement = connection.createStatement()
        ){
            String sql = "CREATE DATABASE IF NOT EXISTS " + dbName;
            statement.executeUpdate(sql);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
