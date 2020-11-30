/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import businesslogiclayer.Assignment;
import java.util.Set;

/**
 *
 * @author avall
 */
public interface AssignmentDAL {
    /**
     * Get all Assignment with a specific week
     * @param week
     * @return 
     */
    public Set<Assignment> getAllForWeek(int week);
}
