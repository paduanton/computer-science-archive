/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.revenda.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author antonio
 */
public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/revenda_db";
    private static final String USER = "root";
    private static final String PASSWORD = "2020";  // Coloque a senha do seu MySQL aqui
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
