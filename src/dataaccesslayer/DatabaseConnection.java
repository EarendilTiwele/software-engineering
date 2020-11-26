/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.invoke.MethodHandles;

/**
 *
 * @author alexd
 */
public class DatabaseConnection {
    
    private static Connection conn;
    
    public static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            String url = "jdbc:postgresql://ec2-54-75-246-118.eu-west-1.compute.amazonaws.com/d4nuqe4269qu7k?sslmode=require";
            Properties props = new Properties();
            props.setProperty("user", "kbhyahfpxyqabj");
            props.setProperty("password", "7fe433219e2003f8119018667ac82205c6164d4d56b0ff5189cf25b1385a49eb");
            props.setProperty("ssl", "true");
            try {
                conn = DriverManager.getConnection(url, props);
            } catch (SQLException ex) {
                Logger.getLogger(MethodHandles.lookup().lookupClass().getSimpleName()).log(Level.SEVERE, null, ex);
            }
        }
        return conn;
    }
    
    public static boolean isClosed() {
        return conn == null;
    }
    
}
