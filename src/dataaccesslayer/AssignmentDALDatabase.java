/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import businesslogiclayer.Assignment;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
    @Override
    public Set<Assignment> getAllForWeek(int week) {
        boolean connectionWasClosed = DatabaseConnection.isClosed();
        Set<Assignment> assignmentSet = new HashSet<>();
       
        return null;
    }
    
}
