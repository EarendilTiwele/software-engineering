/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

import datatransferobjects.Activity;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import dataaccesslayer.ActivityDAO;
import dataaccesslayer.DAOFactory;

/**
 *
 * @author alexd
 */
public class ActivityBO {

    private final ActivityDAO activityDAO;

    public ActivityBO() {
        DAOFactory postgresFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
        activityDAO = postgresFactory.getActivityDAO();
    }

    public int insert(Activity activity) throws SQLException {
        return activityDAO.insert(activity);
    }

    public boolean update(Activity activity) throws SQLException {
        return activityDAO.update(activity);
    }

    public boolean delete(int id) throws SQLException {
        return activityDAO.delete(id);
    }

    public List<Activity> getAll() throws SQLException {
        return new ArrayList<>(activityDAO.getAll());
    }

    public Activity get(int id) throws SQLException {
        return activityDAO.get(id);
    }

    public List<Activity> getAllOfWeek(int week) throws SQLException {
        return new ArrayList<>(activityDAO.getAllOfWeek(week));
    }

    public List<Activity> getAllPlannedOfWeek(int week) throws SQLException {
        return new ArrayList<>(activityDAO.getAllPlannedOfWeek(week));
    }

}
