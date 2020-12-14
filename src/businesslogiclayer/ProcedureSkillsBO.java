/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

import dataaccesslayer.DAOFactory;
import dataaccesslayer.ProcedureDAO;
import dataaccesslayer.ProcedureSkillsDAO;
import datatransferobjects.Competency;
import datatransferobjects.Procedure;
import java.util.Set;

/**
 *
 * @author avall
 */
public class ProcedureSkillsBO {

    private ProcedureSkillsDAO procedureSkillsDAO;

    public ProcedureSkillsBO() {
        DAOFactory postgresFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
        procedureSkillsDAO = postgresFactory.getProcedureSkillsDAO();
    }

    /**
     * Retrieves a <code>Set</code> of <code>Competency</code> objects
     * associated to a <code>procedure</code> from a persistent storage. Returns
     * the <code>Set</code> of <code>Competency</code> objects associated to the
     * <code>procedure</code> if the operation is successful; <code>null</code>
     * otherwise.
     *
     * @param procedure the procedure with the associated competencies
     * @return the <code>Set</code> of <code>Competency</code> objects
     * associated to the <code>procedure</code> if the operation is successful;
     * <code>null</code> otherwise
     */
    public Set<Competency> getAllCompetencies(Procedure procedure) {
        return procedureSkillsDAO.getAllCompetencies(procedure);
    }

}
