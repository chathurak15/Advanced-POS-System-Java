package org.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection dbConnection;
    private final Connection conn;

    private DBConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos","root", "");
    }

    public static DBConnection getInstance() throws ClassNotFoundException, SQLException {
        if(dbConnection == null){
            dbConnection = new DBConnection();

        }
        return dbConnection;
    }

    public Connection getConnection(){
        return conn;
    }

    public void closeConnection() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }

}
