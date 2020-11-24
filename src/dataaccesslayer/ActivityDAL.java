/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import businesslogiclayer.Activity;
import java.sql.ResultSet;

/**
 *
 * @author alexd
 */
public interface ActivityDAL {
    
    public boolean insert (Activity activity);
    public boolean update (Activity activity);
    public boolean delete (int id);
    public ResultSet getAll();
    public ResultSet get(int id);
    public ResultSet getAllOfWeek(int week);
    public ResultSet getAllPlannedOfWeek(int week);
    
}
