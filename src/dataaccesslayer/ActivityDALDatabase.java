/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import businesslogiclayer.Activity;
import businesslogiclayer.PlannedActivity;
import businesslogiclayer.Procedure;
import businesslogiclayer.Site;
import businesslogiclayer.Typology;
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

    /**
     * Insert an activity in the database
     * @param activity
     * @return the version of the activity presents in the database
     * after the insert operation.
     */
    @Override
    public Activity insert(Activity activity) {
        boolean connectionWasClosed = DatabaseConnection.isClosed();
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement prepareStatement = conn.prepareStatement("insert into Activity "
                    + "(id, site, type, description, interventiontime, interruptible, week,"
                    + "workspacenotes, procedure) "
                    + "VALUES (?,?,?,?,?,?,?,?,?)   RETURNING *;");
            prepareStatement.setInt(1, activity.getId());
            prepareStatement.setInt(2, activity.getSite().getId());
            prepareStatement.setInt(3, activity.getTipology().getId());
            prepareStatement.setString(4, activity.getDescription());
            prepareStatement.setInt(5, activity.getInterventionTime());
            prepareStatement.setBoolean(6, activity.isInterruptible());
            prepareStatement.setInt(7, activity.getWeek());
            prepareStatement.setString(8, activity.getWorkspaceNotes());
            prepareStatement.setInt(9, activity.getProcedure().getId());
            ResultSet rs = prepareStatement.executeQuery();
            while (rs.next()) {
                /*-------------------------------------------------------*/
                SiteDAL siteDAL = new SiteDALDatabase();
                int siteId = rs.getInt("site");
                Site site = siteDAL.get(siteId);
                /*-------------------------------------------------------*/
                ProcedureDAL procedureDAL = new ProcedureDALDatabase();
                int procedureId = rs.getInt("procedure");
                Procedure procedure = procedureDAL.get(procedureId);
                /*-------------------------------------------------------*/
                TypologyDAL typologyDAL = new TypologyDALDatabase();
                int typologyId = rs.getInt("type");
                Typology typology = typologyDAL.get(typologyId);
                /*----------------------------------------------------------------*/
                int id = rs.getInt("id");
                String description = rs.getString("description");
                int intervetiontime = rs.getInt("interventiontime");
                boolean interruptible = rs.getBoolean("interruptible");
                int week = rs.getInt("week");
                String workspacenotes = rs.getString("workspacenotes");
                activity = new PlannedActivity(id, site,
                        typology, description, intervetiontime, interruptible,
                        week, procedure, workspacenotes);
            }
            if (connectionWasClosed) {
                conn.close();
            }
            return activity;

        } catch (SQLException ex) {
            Logger.getLogger(ActivityDALDatabase.class.getName()).log(Level.SEVERE, null, ex);
            System.out.print(ex.toString());
        }
        return null;
    }

     /**
     * Update an activity in the database
     * @param activity
     * @return the version of the activity presents in the database
     * after the update operation.
     */
    @Override
    public Activity update(Activity activity) {

        boolean connectionWasClosed = DatabaseConnection.isClosed();
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement prepareStatement;
            prepareStatement = conn.prepareStatement("update Activity "
                    + "SET site = ?, type = ?, description = ?, interventiontime = ?,"
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
                /*-------------------------------------------------------*/
                SiteDAL siteDAL = new SiteDALDatabase();
                int siteId = rs.getInt("site");
                Site site = siteDAL.get(siteId);
                /*-------------------------------------------------------*/
                ProcedureDAL procedureDAL = new ProcedureDALDatabase();
                int procedureId = rs.getInt("procedure");
                Procedure procedure = procedureDAL.get(procedureId);
                /*-------------------------------------------------------*/
                TypologyDAL typologyDAL = new TypologyDALDatabase();
                int typologyId = rs.getInt("type");
                Typology typology = typologyDAL.get(typologyId);
                /*----------------------------------------------------------------*/
                int id = rs.getInt("id");
                String description = rs.getString("description");
                int intervetiontime = rs.getInt("interventiontime");
                boolean interruptible = rs.getBoolean("interruptible");
                int week = rs.getInt("week");
                String workspaceNotes = rs.getString("workspacenotes");
                activity = new PlannedActivity(id, site,
                        typology, description, intervetiontime, interruptible,
                        week, procedure, workspaceNotes);
            }
            if (connectionWasClosed) {
                conn.close();
            }
            return activity;
        } catch (SQLException ex) {
            Logger.getLogger(ActivityDALDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

     /**
     * Delete an activity in the database
     * @param id
     * @return the version of the activity presents in the database
     * before the delete operation.
     */
    @Override
    public Activity delete(int id) {
        boolean connectionWasClosed = DatabaseConnection.isClosed();
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement prepareStatement = conn.prepareStatement("DELETE FROM activity WHERE id=? "
                    + "  RETURNING *; ");
            prepareStatement.setInt(1, id);
            ResultSet rs = prepareStatement.executeQuery();
            Activity activity = null;
            while (rs.next()) {
                /*-------------------------------------------------------*/
                SiteDAL siteDAL = new SiteDALDatabase();
                int siteId = rs.getInt("site");
                Site site = siteDAL.get(siteId);
                /*-------------------------------------------------------*/
                ProcedureDAL procedureDAL = new ProcedureDALDatabase();
                int procedureId = rs.getInt("procedure");
                Procedure procedure = procedureDAL.get(procedureId);
                /*-------------------------------------------------------*/
                TypologyDAL typologyDAL = new TypologyDALDatabase();
                int typologyId = rs.getInt("type");
                Typology typology = typologyDAL.get(typologyId);
                /*----------------------------------------------------------------*/
                id = rs.getInt("id");
                String description = rs.getString("description");
                int intervetiontime = rs.getInt("interventiontime");
                boolean interruptible = rs.getBoolean("interruptible");
                int week = rs.getInt("week");
                activity = new PlannedActivity(id, site,
                        typology, description, intervetiontime, interruptible,
                        week, procedure);
            }
            if (connectionWasClosed) {
                conn.close();
            }
            return activity;

        } catch (SQLException ex) {
            Logger.getLogger(ActivityDALDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Retrieve all activities present in the database
     * @return the list of activities
     */
    @Override
    public List<Activity> getAll() {
        boolean connectionWasClosed = DatabaseConnection.isClosed();
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement prepareStatement = conn.prepareStatement("Select * from activity; ");
            ResultSet rs = prepareStatement.executeQuery();
            List<Activity> activityList = new ArrayList<>();
            Activity activity = null;
            while (rs.next()) {
                /*-------------------------------------------------------*/
                SiteDAL siteDAL = new SiteDALDatabase();
                int siteId = rs.getInt("site");
                Site site = siteDAL.get(siteId);
                /*-------------------------------------------------------*/
                ProcedureDAL procedureDAL = new ProcedureDALDatabase();
                int procedureId = rs.getInt("procedure");
                Procedure procedure = procedureDAL.get(procedureId);
                /*-------------------------------------------------------*/
                TypologyDAL typologyDAL = new TypologyDALDatabase();
                int typologyId = rs.getInt("type");
                Typology typology = typologyDAL.get(typologyId);
                /*----------------------------------------------------------------*/
                int id = rs.getInt("id");
                String description = rs.getString("description");
                int intervetiontime = rs.getInt("interventiontime");
                boolean interruptible = rs.getBoolean("interruptible");
                int week = rs.getInt("week");
                String workspacenotes = rs.getString("workspacenotes");
                activity = new PlannedActivity(id, site,
                        typology, description, intervetiontime, interruptible,
                        week, procedure, workspacenotes);
                activityList.add(activity);
            }
            if (connectionWasClosed) {
                conn.close();
            }
            return activityList;

        } catch (SQLException ex) {
            Logger.getLogger(ActivityDALDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Retrieve the activity with a specified id
     * @param id
     * @return the activity with the specified id
     */
    @Override
    public Activity get(int id) {
        boolean connectionWasClosed = DatabaseConnection.isClosed();
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement prepareStatement = conn.prepareStatement("Select * from activity WHERE id = ? ;");
            prepareStatement.setInt(1, id);
            ResultSet rs = prepareStatement.executeQuery();
            Activity activity = null;
            while (rs.next()) {
                /*-------------------------------------------------------*/
                SiteDAL siteDAL = new SiteDALDatabase();
                int siteId = rs.getInt("site");
                Site site = siteDAL.get(siteId);
                /*-------------------------------------------------------*/
                ProcedureDAL procedureDAL = new ProcedureDALDatabase();
                int procedureId = rs.getInt("procedure");
                Procedure procedure = procedureDAL.get(procedureId);
                /*-------------------------------------------------------*/
                TypologyDAL typologyDAL = new TypologyDALDatabase();
                int typologyId = rs.getInt("type");
                Typology typology = typologyDAL.get(typologyId);
                /*----------------------------------------------------------------*/
                id = rs.getInt("id");
                String description = rs.getString("description");
                int intervetiontime = rs.getInt("interventiontime");
                boolean interruptible = rs.getBoolean("interruptible");
                int week = rs.getInt("week");
                String workspaceNotes = rs.getString("workspacenotes");
                activity = new PlannedActivity(id, site,
                        typology, description, intervetiontime, interruptible,
                        week, procedure, workspaceNotes);
            }
            if (connectionWasClosed) {
                conn.close();
            }
            return activity;

        } catch (SQLException ex) {
            Logger.getLogger(ActivityDALDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Retrieve the activities with a specified week
     * @param week
     * @return the activities with the specified week
     */
    @Override
    public List<Activity> getAllOfWeek(int week) {
        boolean connectionWasClosed = DatabaseConnection.isClosed();
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement prepareStatement = conn.prepareStatement("Select * from activity where week = ?; ");
            prepareStatement.setInt(1, week);
            ResultSet rs = prepareStatement.executeQuery();
            List<Activity> activityList = new ArrayList<>();
            Activity activity = null;
            while (rs.next()) {
                /*-------------------------------------------------------*/
                SiteDAL siteDAL = new SiteDALDatabase();
                int siteId = rs.getInt("site");
                Site site = siteDAL.get(siteId);
                /*-------------------------------------------------------*/
                ProcedureDAL procedureDAL = new ProcedureDALDatabase();
                int procedureId = rs.getInt("procedure");
                Procedure procedure = procedureDAL.get(procedureId);
                /*-------------------------------------------------------*/
                TypologyDAL typologyDAL = new TypologyDALDatabase();
                int typologyId = rs.getInt("type");
                Typology typology = typologyDAL.get(typologyId);
                /*----------------------------------------------------------------*/
                int id = rs.getInt("id");
                String description = rs.getString("description");
                int intervetiontime = rs.getInt("interventiontime");
                boolean interruptible = rs.getBoolean("interruptible");
                int week1 = rs.getInt("week");
                String workspacenotes = rs.getString("workspacenotes");
                activity = new PlannedActivity(id, site,
                        typology, description, intervetiontime, interruptible,
                        week1, procedure, workspacenotes);
                activityList.add(activity);
            }
            if (connectionWasClosed) {
                conn.close();
            }
            return activityList;

        } catch (SQLException ex) {
            Logger.getLogger(ActivityDALDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Retrieve the planned activities with a specified week
     * @param week
     * @return 
     */
    @Override
    public List<Activity> getAllPlannedOfWeek(int week) {
        return getAllOfWeek(week);
    }
    
    /**
     * Delete all activities from the database
     * @return the list of activities before the delete operation
     */
    @Override
    public List<Activity> deleteAll(){
     boolean connectionWasClosed = DatabaseConnection.isClosed();
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement prepareStatement = conn.prepareStatement("delete from activity RETURNING *; ");
            ResultSet rs = prepareStatement.executeQuery();
            List<Activity> activityList = new ArrayList<>();
            Activity activity = null;
            while (rs.next()) {
                /*-------------------------------------------------------*/
                SiteDAL siteDAL = new SiteDALDatabase();
                int siteId = rs.getInt("site");
                Site site = siteDAL.get(siteId);
                /*-------------------------------------------------------*/
                ProcedureDAL procedureDAL = new ProcedureDALDatabase();
                int procedureId = rs.getInt("procedure");
                Procedure procedure = procedureDAL.get(procedureId);
                /*-------------------------------------------------------*/
                TypologyDAL typologyDAL = new TypologyDALDatabase();
                int typologyId = rs.getInt("type");
                Typology typology = typologyDAL.get(typologyId);
                /*----------------------------------------------------------------*/
                int id = rs.getInt("id");
                String description = rs.getString("description");
                int intervetiontime = rs.getInt("interventiontime");
                boolean interruptible = rs.getBoolean("interruptible");
                int week1 = rs.getInt("week");
                String workspacenotes = rs.getString("workspacenotes");
                activity = new PlannedActivity(id, site,
                        typology, description, intervetiontime, interruptible,
                        week1, procedure, workspacenotes);
                activityList.add(activity);
            }
            if (connectionWasClosed) {
                conn.close();
            }
            return activityList;

        } catch (SQLException ex) {
            Logger.getLogger(ActivityDALDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
