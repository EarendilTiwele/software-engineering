/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import businesslogiclayer.Activity;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Francesco Avallone
 */
public class ActivityDALDatabase implements ActivityDAL{
    
    private Connection c;
    
    public Connection getConnectionObj()
    {
        
        String url = "jdbc:postgresql://ec2-54-75-246-118.eu-west-1.compute.amazonaws.com/d4nuqe4269qu7k?sslmode=require";
        Properties props = new Properties();
        props.setProperty("user", "kbhyahfpxyqabj");
        props.setProperty("password", "7fe433219e2003f8119018667ac82205c6164d4d56b0ff5189cf25b1385a49eb");
        props.setProperty("ssl", "true");
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, props);
        } catch (SQLException ex) {
            Logger.getLogger(ActivityDALDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }
    
    
    @Override
    public boolean insert(Activity activity) {
        try {
        
        c = getConnectionObj();
        
        c.close();
        } catch (SQLException ex) {
            Logger.getLogger(ActivityDALDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean update(Activity activity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean delete(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Activity> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Activity get(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    @Override
    public List<Activity> getAllOfWeek(int week) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Activity> getAllPlannedOfWeek(int week) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
