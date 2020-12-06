/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

import dataaccesslayer.ActivityDAL;
import dataaccesslayer.ActivityDALDatabase;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alexd
 */
public class ActivityBLL {
    
    private final ActivityDAL activityDAL;

    public ActivityBLL() {
        activityDAL = new ActivityDALDatabase();
    }

    public Activity insert(Activity activity) throws SQLException {
        return activityDAL.insert(activity);
    }

    public Activity update(Activity activity) throws SQLException {
        return activityDAL.update(activity);
    }

    public Activity delete(int id) throws SQLException {
        return activityDAL.delete(id);
    }
    
    public List<Activity> getAll() throws SQLException {
        return new ArrayList<>(activityDAL.getAll());
    }
    
    public Activity get(int id) throws SQLException {
        return activityDAL.get(id);
    }
    
    public List<Activity> getAllOfWeek(int week) throws SQLException {
       return new ArrayList<>(activityDAL.getAllOfWeek(week));
    }
    
    public List<Activity> getAllPlannedOfWeek(int week) throws SQLException{
        return new ArrayList<>(activityDAL.getAllPlannedOfWeek(week));
    }
    
}
