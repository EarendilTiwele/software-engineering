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
import java.sql.SQLException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Francesco Avallone
 */
public class PostgresActivityDAO extends PostgresAbstractDAO<Activity> implements ActivityDAO {

    /**
     * Returns the <code>Activity</code> object builded on the current row of
     * the ResultSet <code>rs</code>.
     *
     * @param rs the ResultSet with which to build the <code>Activity</code>
     * object
     * @return the <code>Activity</code> object builded on the current row of
     * the ResultSet <code>rs</code>
     * @throws SQLException if a database access error occurs
     */
    @Override
    Activity convertToEntity(ResultSet rs) throws SQLException {
        SiteDAO siteDAL = new PostgresSiteDAO();
        int siteId = rs.getInt("site");
        Site site = siteDAL.get(siteId);
        /*-------------------------------------------------------*/
        ProcedureDAO procedureDAL = new PostgresProcedureDAO();
        int procedureId = rs.getInt("procedure");
        Procedure procedure = procedureDAL.get(procedureId);
        ProcedureSkillsDAO procedureHasCompetencies = new PostgresProcedureSkillsDAO();
        for (Competency competency : procedureHasCompetencies.getAllCompetencies(procedure)) {
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
     * Inserts a <code>Activity</code> object in the database. Returns the id of
     * the inserted <code>activity</code> if the operation is successful; -1
     * otherwise.
     *
     * @param activity the <code>Activity</code> object to insert
     * @return the id of the inserted <code>Activity</code> if the operation is
     * successful; -1 otherwise
     */
    @Override
    public int insert(Activity activity) {
        String query = String.format("insert into Activity "
                + "( site, type, description, interventiontime, interruptible, week, "
                + "workspacenotes, procedure) "
                + "VALUES (%d,%d,'%s',%d,%b,%d,'%s',%d)   RETURNING *;",
                activity.getSite().getId(), activity.getTipology().getId(),
                activity.getDescription(), activity.getInterventionTime(), activity.isInterruptible(),
                activity.getWeek(), activity.getWorkspaceNotes(), activity.getProcedure().getId());
        try {
            return executeQuery(query).getId();
        } catch (SQLException ex) {
            Logger.getLogger(PostgresActivityDAO.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    /**
     * Updates a <code>Activity</code> object in the database, if the
     * <code>Activity</code> is not present in the database it is not created.
     * Returns <code>true</code> if the operation is successful, that is both
     * when the <code>activity</code> is updated and when the
     * <code>activity</code> doesn't exist in the database; <code>false</code>
     * otherwise.
     *
     * @param activity the <code>Activity</code> object to update
     * @return <code>true</code> if the operation is successful, that is both
     * when the activity is updated and when the activity doesn't exist in the
     * database; <code>false</code> otherwise
     */
    @Override
    public boolean update(Activity activity) {
        String query = String.format("update Activity "
                + "SET site = %d, type = %d, description = '%s', interventiontime = %d,"
                + "interruptible = %b, week = %d, workspacenotes = '%s', procedure = %d"
                + "WHERE id = %d; ",
                activity.getSite().getId(), activity.getTipology().getId(), activity.getDescription(),
                activity.getInterventionTime(), activity.isInterruptible(), activity.getWeek(),
                activity.getWorkspaceNotes(), activity.getProcedure().getId(), activity.getId());
        try {
            executeUpdate(query);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(PostgresActivityDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Deletes the activity with given <code>id</code> from a database. Returns
     * <code>true</code> if the operation is successful, that is both when the
     * activity with given <code>id</code> is deleted and when the activity with
     * given <code>id</code> doesn't exist in the database; <code>false</code>
     * otherwise.
     *
     * @param id the <code>id</code> which identifies the activity
     * @return <code>true</code> if the operation is successful, that is both
     * when the activity with given <code>id</code> is deleted and when the
     * activity with given <code>id</code> doesn't exist in the database;
     * <code>false</code> otherwise
     */
    @Override
    public boolean delete(int id) {
        String query = String.format("DELETE FROM activity WHERE id=%d; ", id);
        try {
            executeUpdate(query);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(PostgresActivityDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Retrieves a <code>Set</code> of <code>Activity</code> objects from a
     * database. Returns the <code>Set</code> of <code>Activity</code> objects
     * if the operation is successful; <code>null</code> otherwise.
     *
     * @return the <code>Set</code> of <code>Activity</code> objects from the
     * database if the operation is successful; <code>null</code> otherwise
     */
    @Override
    public Set<Activity> getAll() {
        String query = "Select * from activity";
        try {
            return executeSetQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(PostgresActivityDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Retrieves the <code>Activity</code> object with given <code>id</code>
     * from a database. Returns the <code>Activity</code> object with given
     * <code>id</code> if it exists in the database; <code>null</code> if the
     * <code>Activity</code> object with given <code>id</code> doesn't exist in
     * the database or if the operation fails.
     *
     * @param id the id which identifies the activity
     * @return the <code>Activity</code> object with given <code>id</code> if it
     * exists in the database, returns <code>null</code> if the
     * <code>Activity</code> object with given <code>id</code> doesn't exist in
     * the database or if the operation fails
     */
    @Override
    public Activity get(int id) {
        String query = String.format("Select * from activity WHERE id = %d ;", id);
        try {
            return executeQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(PostgresActivityDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Retrieves a <code>Set</code> of <code>Activity</code> objects with a
     * specific <code>week</code> from a database. Returns the <code>Set</code>
     * of <code>Activity</code> objects if the operation is successful;
     * <code>null</code> otherwise.
     *
     * @param week
     * @return the <code>Set</code> of <code>Activity</code> objects with a
     * specific week from the database if the operation is successful;
     * <code>null</code> otherwise
     */
    @Override
    public Set<Activity> getAllOfWeek(int week) {
        String query = String.format("Select * from activity where week = %d;", week);
        try {
            return executeSetQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(PostgresActivityDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Retrieves a <code>Set</code> of <code>PlannedActivity</code> objects with
     * a specific <code>week</code> from a database. Returns the
     * <code>Set</code> of <code>PlannedActivity</code> objects if the operation
     * is successful; <code>null</code> otherwise.
     *
     * @param week
     * @return the <code>Set</code> of <code>Activity</code> objects with a
     * specific week from the database if the operation is successful;
     * <code>null</code> otherwise
     */
    @Override
    public Set<Activity> getAllPlannedOfWeek(int week) {
        return getAllOfWeek(week);
    }
}
