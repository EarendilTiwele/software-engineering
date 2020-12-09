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

    @Override
    public Assignment convertToEntity(ResultSet rs) throws SQLException {
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

}
