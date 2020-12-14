/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer.postgres;

import datatransferobjects.Activity;
import datatransferobjects.Assignment;
import datatransferobjects.Competency;
import datatransferobjects.Maintainer;
import datatransferobjects.User;
import dataaccesslayer.ActivityDAO;
import dataaccesslayer.AssignmentDAO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author avall
 */
public class PostgresAssignmentDAO extends PostgresAbstractDAO<Assignment> implements AssignmentDAO {

    /**
     * Returns the <code>Assignment</code> object builded on the current row of
     * the ResultSet <code>rs</code>.
     *
     * @param rs the ResultSet with which to build the <code>Assignment</code>
     * object
     * @return the <code>Assignment</code> object builded on the current row of
     * the ResultSet <code>rs</code>
     * @throws SQLException if a database access error occurs
     */
    @Override
    Assignment convertToEntity(ResultSet rs) throws SQLException {
        ActivityDAO activityDAL = new PostgresActivityDAO();
        PostgresUserDAO userDAL = new PostgresUserDAO();
        int idactivity = rs.getInt("idactivity");
        Activity activity = activityDAL.get(idactivity);
        User user = userDAL.get(rs.getInt("idmaintainer"));
        Maintainer maintainer = new Maintainer(user.getId(), user.getUsername(), user.getPassword());
        PostgresMaintainerSkillsDAO mhcDAL = new PostgresMaintainerSkillsDAO();
        Set<Competency> competencySet = null;
        competencySet = mhcDAL.getAllCompetencies(maintainer);
        for (Competency c : competencySet) {
            maintainer.addCompetency(c);
        }
        Assignment assignment = new Assignment(maintainer, activity, rs.getString("day"), rs.getInt("hour"));
        return assignment;
    }

    /**
     * Retrieves a <code>Set</code> of <code>Assignment</code> objects from the
     * Postgres Database. Returns the <code>Set</code> of
     * <code>Assignment</code> objects if the operation is successful;
     * <code>null</code> otherwise.
     *
     * @return the <code>Set</code> of <code>Assignment</code> objects from the
     * Postgres Database if the operation is successful; <code>null</code>
     * otherwise
     */
    @Override
    public Set<Assignment> getAllForWeek(int week) {
        String query = String.format("select idmaintainer, idactivity, day,hour from"
                + " assignment inner join activity on idactivity = activity.id"
                + " where week = %d;", week);
        try {
            return executeSetQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(PostgresAssignmentDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Inserts a <code>Assignment</code> object in the Postgres Database.
     * Returns <code>true</code> if the operation is successful;
     * <code>false</code> otherwise.
     *
     * @param assignment the <code>Assignment</code> object to insert.
     * @return <code>true</code> if the operation is successful;
     * <code>false</code> otherwise.
     */
    @Override
    public boolean insert(Assignment assignment) {
        String query = String.format("Insert into assignment values (%d, %d, '%s', %d);",
                assignment.getMaintainer().getId(), assignment.getActivity().getId(),
                assignment.getDay(), assignment.getHour());
        try {
            executeUpdate(query);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(PostgresAssignmentDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

}
