/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import datatransferobjects.Competency;
import datatransferobjects.Procedure;
import java.util.Set;

/**
 *
 * @author avall
 */
public interface ProcedureSkillsDAO {

    /**
     * Retrieves a <code>Set</code> of <code>Competency</code>, associated with
     * a specific <code>Procedure</code> object, from a persistent storage.
     * Returns the <code>Set</code> of <code>Competency</code> objects if the
     * operation is successful; <code>null</code> otherwise.
     *
     * @param procedure
     * @return the <code>Set</code> of <code>Competency</code> objects from the
     * persistent storage if the operation is successful; <code>null</code>
     * otherwise.
     */
    public Set<Competency> getAllCompetencies(Procedure procedure);
}
