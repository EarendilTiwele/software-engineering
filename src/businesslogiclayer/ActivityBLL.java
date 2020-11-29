/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

import dataaccesslayer.ActivityDAL;
import dataaccesslayer.ActivityDALDatabase;
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

    public Activity insert(Activity activity) {
        return activityDAL.insert(activity);
    }

    public Activity update(Activity activity) {
        return activityDAL.update(activity);
    }

    public Activity delete(int id) {
        return activityDAL.delete(id);
    }
    
    public List<Activity> getAll() {
        return activityDAL.getAll();
    }
    
    public Activity get(int id) {
        return activityDAL.get(id);
    }
    
    public List<Activity> getAllOfWeek(int week) {
        return activityDAL.getAllOfWeek(week);
    }
    
    public List<Activity> getAllPlannedOfWeek(int week){
        return activityDAL.getAllPlannedOfWeek(week);
    }
    
}
