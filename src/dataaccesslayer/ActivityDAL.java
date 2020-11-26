/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import businesslogiclayer.Activity;
import java.sql.ResultSet;
import java.util.List;

/**
 *
 * @author alexd
 */
public interface ActivityDAL {
    
    public Activity insert (Activity activity);
    public Activity update (Activity activity);
    public Activity delete (int id);
    public List<Activity> getAll();
    public Activity get(int id);
    public List<Activity> getAllOfWeek(int week);
    public List<Activity> getAllPlannedOfWeek(int week);
    
}
