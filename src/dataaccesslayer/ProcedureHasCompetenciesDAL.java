/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import businesslogiclayer.Competency;
import businesslogiclayer.Procedure;
import java.util.Set;

/**
 *
 * @author avall
 */
public interface ProcedureHasCompetenciesDAL {
    /**
     * Get all competencies associated with a specific procedure
     * @param procedure
     * @return the competencies of the procedure
     */
    public Set<Competency> getAllCompetencies(Procedure procedure);
}
