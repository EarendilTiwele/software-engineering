/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer.postgres;

import datatransferobjects.Activity;
import datatransferobjects.Competency;
import datatransferobjects.PlannedActivity;
import datatransferobjects.Procedure;
import datatransferobjects.Site;
import datatransferobjects.Typology;
import dataaccesslayer.ActivityDAO;
import dataaccesslayer.ProcedureDAO;
import dataaccesslayer.ProcedureSkillsDAO;
import dataaccesslayer.SiteDAO;
import dataaccesslayer.TypologyDAO;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Francesco Avallone
 */
public class PostgresActivityDAO extends PostgresAbstractDAO<Activity> implements ActivityDAO {

    private Connection conn;

    @Override
    public Activity convertToEntity(ResultSet rs) throws SQLException {
        SiteDAO siteDAL = new PostgresSiteDAO();
        int siteId = rs.getInt("site");
        Site site = siteDAL.get(siteId);
        /*-------------------------------------------------------*/
        ProcedureDAO procedureDAL = new PostgresProcedureDAO();
        int procedureId = rs.getInt("procedure");
        Procedure procedure = procedureDAL.get(procedureId);
        ProcedureSkillsDAO procedureHasCompetencies = new PostgresProcedureSkillsDAO();
        for (Competency competency : procedureHasCompetencies.getAllCompetencies(procedure))
        {
            procedure.addCompetency(competency);
        }
        /*-------------------------------------------------------*/
        TypologyDAO typologyDAL = new PostgresTypologyDAO();
        int typologyId = rs.getInt("type");
        Typology typology = typologyDAL.get(typologyId);
        /*----------------------------------------------------------------*/
        int id = rs.getInt("id");
        String description = rs.getString("description");
        int intervetiontime = rs.getInt("interventiontime");
        boolean interruptible = rs.getBoolean("interruptible");
        int week = rs.getInt("week");
        String workspacenotes = rs.getString("workspacenotes");
        Activity activity = new PlannedActivity(id, site,
                typology, description, intervetiontime, interruptible,
                week, procedure, workspacenotes);
        return activity;
    }

    /**
     * Insert an activity in the database
     *
     * @param activity
     * @return the version of the activity presents in the database after the
     * insert operation.
     * @throws java.sql.SQLException
     */
    @Override
    public Activity insert(Activity activity) throws SQLException {
        String query = String.format("insert into Activity "
                + "(id, site, type, description, interventiontime, interruptible, week, "
                + "workspacenotes, procedure) "
                + "VALUES (%d,%d,%d,'%s',%d,%b,%d,'%s',%d)   RETURNING *;",
                activity.getId(), activity.getSite().getId(), activity.getTipology().getId(),
                activity.getDescription(), activity.getInterventionTime(), activity.isInterruptible(),
                activity.getWeek(), activity.getWorkspaceNotes(), activity.getProcedure().getId());
        return executeQuery(query);
    }

    /**
     * Update an activity in the database
     *
     * @param activity
     * @return the version of the activity presents in the database after the
     * update operation.
     * @throws java.sql.SQLException
     */
    @Override
    public Activity update(Activity activity) throws SQLException {
        String query = String.format("update Activity "
                + "SET site = %d, type = %d, description = '%s', interventiontime = %d,"
                + "interruptible = %b, week = %d, workspacenotes = '%s', procedure = %d"
                + "WHERE id = %d  RETURNING *; ",
                activity.getSite().getId(), activity.getTipology().getId(), activity.getDescription(),
                activity.getInterventionTime(), activity.isInterruptible(), activity.getWeek(),
                activity.getWorkspaceNotes(), activity.getProcedure().getId(), activity.getId());
        return executeQuery(query);
    }

    /**
     * Delete an activity in the database
     *
     * @param id
     * @return the version of the activity presents in the database before the
     * delete operation.
     * @throws java.sql.SQLException
     */
    @Override
    public Activity delete(int id) throws SQLException {
        String query = String.format("DELETE FROM activity WHERE id=%d RETURNING *; ", id);
        return executeQuery(query);
    }

    /**
     * Retrieve all activities present in the database
     *
     * @return the list of activities
     */
    @Override
    public Set<Activity> getAll() throws SQLException {
        String query = "Select * from activity";
        return executeSetQuery(query);
    }

    /**
     * Retrieve the activity with a specified id
     *
     * @param id
     * @return the activity with the specified id
     * @throws java.sql.SQLException
     */
    @Override
    public Activity get(int id) throws SQLException {
//        boolean connectionWasClosed = DatabaseConnection.isClosed();
//        try {
//            conn = DatabaseConnection.getConnection();
        String query = String.format("Select * from activity WHERE id = %d ;", id);
        return executeQuery(query);
//            PreparedStatement prepareStatement = conn.prepareStatement("Select * from activity WHERE id = ? ;");
//            prepareStatement.setInt(1, id);
//            ResultSet rs = prepareStatement.executeQuery();
//            Activity activity = null;
//            while (rs.next()) {
//                /*-------------------------------------------------------*/
//                SiteDAO siteDAL = new PostgresSiteDAO();
//                int siteId = rs.getInt("site");
//                Site site = siteDAL.get(siteId);
//                /*-------------------------------------------------------*/
//                ProcedureDAO procedureDAL = new PostgresProcedureDAO();
//                int procedureId = rs.getInt("procedure");
//                Procedure procedure = procedureDAL.get(procedureId);
//                /*-------------------------------------------------------*/
//                TypologyDAO typologyDAL = new PostgresTypologyDAO();
//                int typologyId = rs.getInt("type");
//                Typology typology = typologyDAL.get(typologyId);
//                /*----------------------------------------------------------------*/
//                id = rs.getInt("id");
//                String description = rs.getString("description");
//                int intervetiontime = rs.getInt("interventiontime");
//                boolean interruptible = rs.getBoolean("interruptible");
//                int week = rs.getInt("week");
//                String workspaceNotes = rs.getString("workspacenotes");
//                activity = new PlannedActivity(id, site,
//                        typology, description, intervetiontime, interruptible,
//                        week, procedure, workspaceNotes);
//            }
//            if (connectionWasClosed) {
//                conn.close();
//            }
//            return activity;
//
//        } catch (SQLException ex) {
//            Logger.getLogger(PostgresActivityDAO.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
    }

    /**
     * Retrieve the activities with a specified week
     *
     * @param week
     * @return the activities with the specified week
     */
    @Override
    public Set<Activity> getAllOfWeek(int week) throws SQLException {
        String query = String.format("Select * from activity where week = %d;", week);
        return executeSetQuery(query);
    }

    /**
     * Retrieve the planned activities with a specified week
     *
     * @param week
     * @return
     */
    @Override
    public Set<Activity> getAllPlannedOfWeek(int week) throws SQLException {
        return getAllOfWeek(week);
    }

    /**
     * Delete all activities from the database
     *
     * @return the list of activities before the delete operation
     */
    @Override
    public Set<Activity> deleteAll() throws SQLException {
        String query = String.format("delete from activity RETURNING *; ");
        return executeSetQuery(query);
    }

}
