/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import businesslogiclayer.Activity;
import businesslogiclayer.PlannedActivity;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Francesco Avallone
 */
public class ActivityDALDatabase implements ActivityDAL {

    private Connection conn;

    public Connection getConnectionObj() {

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
    public Activity insert(Activity activity) {

        try {

            conn = getConnectionObj();
            PreparedStatement prepareStatement = conn.prepareStatement("insert into Activity "
                    + "(site, type, description, interventiontime, interruptible, week,"
                    + "workspacenotes, procedure) "
                    + "VALUES (?,?,?,?,?,?,?,?)   RETURNING *;");
            prepareStatement.setInt(1, activity.getSite().getId());
            prepareStatement.setInt(2, activity.getTipology().getId());
            prepareStatement.setString(3, activity.getDescription());
            prepareStatement.setInt(4, activity.getInterventionTime());
            prepareStatement.setBoolean(5, activity.isInterruptible());
            prepareStatement.setInt(6, activity.getWeek());
            prepareStatement.setString(7, activity.getWorkspaceNotes());
            prepareStatement.setInt(8, activity.getProcedure().getId());
            ResultSet rs = prepareStatement.executeQuery();
            while (rs.next()) {
                /* It's necessary to have the others DAL implementation to rebuild the object Site, Typology and Procedure
                inside the Activity obj*/
                /*The same operations must be done with Typology and Procedure obj*/
                SiteDAL siteDAL = new SiteDALDatabase();
                int siteId = rs.getInt("site");
                /*----------------------------------------------------------------*/
                activity = new PlannedActivity(rs.getInt("id"), siteDAL.get(siteId),
                        activity.getTipology(), rs.getString("description"), rs.getInt("intervationtime"),
                        rs.getBoolean("interruptible"), rs.getInt("week"), activity.getProcedure());
            }
            conn.close();
            return activity;

        } catch (SQLException ex) {
            Logger.getLogger(ActivityDALDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Activity update(Activity activity) {
        conn = getConnectionObj();
        PreparedStatement prepareStatement;
        try {
            prepareStatement = conn.prepareStatement("update Activity "
                    + "SET site = ?, type = ?, description = ?, interventiontime = ?"
                    + "interruptible = ?, week = ?, workspacenotes = ?, procedure = ?"
                    + "WHERE id = ?  RETURNING *; ");

            prepareStatement.setInt(1, activity.getSite().getId());
            prepareStatement.setInt(2, activity.getTipology().getId());
            prepareStatement.setString(3, activity.getDescription());
            prepareStatement.setInt(4, activity.getInterventionTime());
            prepareStatement.setBoolean(5, activity.isInterruptible());
            prepareStatement.setInt(6, activity.getWeek());
            prepareStatement.setString(7, activity.getWorkspaceNotes());
            prepareStatement.setInt(8, activity.getProcedure().getId());
            prepareStatement.setInt(9, activity.getId());
            ResultSet rs = prepareStatement.executeQuery();
            while (rs.next()) {
                /* It's necessary to have the others DAL implementation to rebuild the object Site, Typology and Procedure
                inside the Activity obj*/
                /*The same operations must be done with Typology and Procedure obj*/
                SiteDAL siteDAL = new SiteDALDatabase();
                int siteId = rs.getInt("site");
                /*----------------------------------------------------------------*/
                activity = new PlannedActivity(rs.getInt("id"), siteDAL.get(siteId),
                        activity.getTipology(), rs.getString("description"), rs.getInt("intervationtime"),
                        rs.getBoolean("interruptible"), rs.getInt("week"), activity.getProcedure());
            }
            conn.close();
            return activity;
        } catch (SQLException ex) {
            Logger.getLogger(ActivityDALDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public Activity delete(int id) {
        try {
            conn = getConnectionObj();
            PreparedStatement prepareStatement = conn.prepareStatement("DELETE FROM activity WHERE id=?;"
                    + "  RETURNING *; ");
            prepareStatement.setInt(1, id);
            ResultSet rs = prepareStatement.executeQuery();
            while (rs.next()) {
                /* It's necessary to have the others DAL implementation to rebuild the object Site, Typology and Procedure
                inside the Activity obj*/
                /*The same operations must be done with Typology and Procedure obj*/
                SiteDAL siteDAL = new SiteDALDatabase();
                int siteId = rs.getInt("site");
                /*----------------------------------------------------------------*/
                /*Activity activity = new PlannedActivity(rs.getInt("id"), siteDAL.get(siteId),
                        activity.getTipology(), rs.getString("description"), rs.getInt("intervationtime"),
                        rs.getBoolean("interruptible"), rs.getInt("week"), activity.getProcedure());*/
            }
            conn.close();
            /*return activity*/

        } catch (SQLException ex) {
            Logger.getLogger(ActivityDALDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<Activity> getAll() {
        try {
            conn = getConnectionObj();
            PreparedStatement prepareStatement = conn.prepareStatement("Select * from activity; ");
            ResultSet rs = prepareStatement.executeQuery();
            List<Activity> activityList = new ArrayList<>();
            while (rs.next()) {
                /* It's necessary to have the others DAL implementation to rebuild the object Site, Typology and Procedure
                inside the Activity obj*/
                /*The same operations must be done with Typology and Procedure obj*/
                SiteDAL siteDAL = new SiteDALDatabase();
                int siteId = rs.getInt("site");
                /*----------------------------------------------------------------*/
                /*Activity activity = new PlannedActivity(rs.getInt("id"), siteDAL.get(siteId),
                        activity.getTipology(), rs.getString("description"), rs.getInt("intervationtime"),
                        rs.getBoolean("interruptible"), rs.getInt("week"), activity.getProcedure());*/
            }
            conn.close();
            return activityList;

        } catch (SQLException ex) {
            Logger.getLogger(ActivityDALDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Activity get(int id) {
        try {
            conn = getConnectionObj();
            PreparedStatement prepareStatement = conn.prepareStatement("Select from activity WHERE id = ? ;"
                    + "  RETURNING *; ");
            prepareStatement.setInt(1, id);
            ResultSet rs = prepareStatement.executeQuery();
            while (rs.next()) {
                /* It's necessary to have the others DAL implementation to rebuild the object Site, Typology and Procedure
                inside the Activity obj*/
                /*The same operations must be done with Typology and Procedure obj*/
                SiteDAL siteDAL = new SiteDALDatabase();
                int siteId = rs.getInt("site");
                /*----------------------------------------------------------------*/
                /*Activity activity = new PlannedActivity(rs.getInt("id"), siteDAL.get(siteId),
                        activity.getTipology(), rs.getString("description"), rs.getInt("intervationtime"),
                        rs.getBoolean("interruptible"), rs.getInt("week"), activity.getProcedure());*/
            }
            conn.close();
            /*return activity*/

        } catch (SQLException ex) {
            Logger.getLogger(ActivityDALDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<Activity> getAllOfWeek(int week) {
        try {
            conn = getConnectionObj();
            PreparedStatement prepareStatement = conn.prepareStatement("Select * from activity where week = ?; ");
            prepareStatement.setInt(1, week);
            ResultSet rs = prepareStatement.executeQuery();
            List<Activity> activityList = new ArrayList<>();
            while (rs.next()) {
                /* It's necessary to have the others DAL implementation to rebuild the object Site, Typology and Procedure
                inside the Activity obj*/
                /*The same operations must be done with Typology and Procedure obj*/
                SiteDAL siteDAL = new SiteDALDatabase();
                int siteId = rs.getInt("site");
                /*----------------------------------------------------------------*/
                /*Activity activity = new PlannedActivity(rs.getInt("id"), siteDAL.get(siteId),
                        activity.getTipology(), rs.getString("description"), rs.getInt("intervationtime"),
                        rs.getBoolean("interruptible"), rs.getInt("week"), activity.getProcedure());*/
            }
            conn.close();
            return activityList;

        } catch (SQLException ex) {
            Logger.getLogger(ActivityDALDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<Activity> getAllPlannedOfWeek(int week) {
        return getAllOfWeek(week);
    }
}
