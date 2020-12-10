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
import java.sql.SQLException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Francesco Avallone
 */
public class PostgresActivityDAO extends PostgresAbstractDAO<Activity> implements ActivityDAO {

    private Connection conn;

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

    @Override
    public Set<Activity> getAllPlannedOfWeek(int week) {
        return getAllOfWeek(week);
    }
}
