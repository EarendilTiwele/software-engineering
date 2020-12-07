/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import datatransferobjects.Competency;
import datatransferobjects.Procedure;
import java.sql.SQLException;
import java.util.Set;

/**
 *
 * @author avall
 */
public interface ProcedureSkillsDAO {
    /**
     * Get all competencies associated with a specific procedure
     * @param procedure
     * @return the competencies of the procedure
     * @throws java.sql.SQLException
     */
    public Set<Competency> getAllCompetencies(Procedure procedure) throws SQLException;
}
