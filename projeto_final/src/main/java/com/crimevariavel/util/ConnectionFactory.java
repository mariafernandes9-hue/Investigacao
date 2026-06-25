package com.crimevariavel.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    
    private static final String URL     = "jdbc:postgresql://localhost:5432/investigacao";
    private static final String USUARIO = "postgres";
    private static final String SENHA   = "postgres";
    
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
