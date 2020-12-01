/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import businesslogiclayer.Activity;
import businesslogiclayer.Assignment;
import businesslogiclayer.Maintainer;
import businesslogiclayer.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author avall
 */
public class AssignmentDALDatabase implements AssignmentDAL {

    private Connection conn;

    /**
     * Get all Assignment with a specific week
     * @param week
     * @return 
     */
    @Override
    public Set<Assignment> getAllForWeek(int week) {
        boolean connectionWasClosed = DatabaseConnection.isClosed();
        Set<Assignment> assignmentSet = new HashSet<>();
        ActivityDAL activityDAL = new ActivityDALDatabase();
        UserDALDatabase userDAL = new UserDALDatabase();
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement prepareStatement
                    = conn.prepareStatement("select idmaintainer, idactivity, day,hour from"
                            + " assignment inner join activity on idactivity = activity.id"
                            + " where week = ?; ");
            prepareStatement.setInt(1, week);
            ResultSet rs = prepareStatement.executeQuery();
            Assignment assignment = null;
            Activity activity = null;
            User user = null;
            Maintainer maintainer = null;
            while (rs.next()) {
                int idactivity = rs.getInt("idactivity");
                activity = activityDAL.get(idactivity);
                user = userDAL.get(rs.getInt("idmaintainer"));
                maintainer = new Maintainer(user.getId(), user.getUsername(), user.getPassword());
                assignment = new Assignment(maintainer, activity, rs.getString("day"), rs.getInt("hour"));
                assignmentSet.add(assignment);
            }
            if (connectionWasClosed) {
                conn.close();
            }
            return assignmentSet;
        } catch (SQLException ex) {
            Logger.getLogger(SiteDALDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

}
