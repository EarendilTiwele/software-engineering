/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import businesslogiclayer.Activity;
import businesslogiclayer.Assignment;
import businesslogiclayer.Competency;
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
public class AssignmentDALDatabase extends AbstractDAL<Assignment> implements AssignmentDAL {

    @Override
    public Assignment convertToEntity(ResultSet rs) throws SQLException {
        ActivityDAL activityDAL = new ActivityDALDatabase();
        UserDALDatabase userDAL = new UserDALDatabase();
        int idactivity = rs.getInt("idactivity");
        Activity activity = activityDAL.get(idactivity);
        User user = userDAL.get(rs.getInt("idmaintainer"));
        Maintainer maintainer = new Maintainer(user.getId(), user.getUsername(), user.getPassword());
        MaintainerHasCompetenciesDALDatabase mhcDAL = new MaintainerHasCompetenciesDALDatabase();
        Set<Competency> competencySet = null;
        competencySet = mhcDAL.getAllCompetencies(maintainer);
        for (Competency c : competencySet) {
            maintainer.addCompetency(c);
        }
        Assignment assignment = new Assignment(maintainer, activity, rs.getString("day"), rs.getInt("hour"));
        return assignment;
    }

    /**
     * Get all Assignment with a specific week
     *
     * @param week
     * @return
     */
    @Override
    public Set<Assignment> getAllForWeek(int week) throws SQLException {
            String query = String.format("select idmaintainer, idactivity, day,hour from"
                    + " assignment inner join activity on idactivity = activity.id"
                    + " where week = %d;", week);
            return executeSetQuery(query);
    }

}
