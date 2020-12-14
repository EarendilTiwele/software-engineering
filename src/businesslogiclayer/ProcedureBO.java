/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

import datatransferobjects.Procedure;
import dataaccesslayer.DAOFactory;
import java.util.ArrayList;
import java.util.List;
import dataaccesslayer.ProcedureDAO;
import datatransferobjects.Competency;
import java.util.Set;

/**
 *
 * @author avall
 */
public class ProcedureBO {

    private final ProcedureDAO procedureDAO;

    public ProcedureBO() {
        DAOFactory postgresFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
        procedureDAO = postgresFactory.getProcedureDAO();
    }

    public int insert(Procedure procedure) {
        return procedureDAO.insert(procedure);
    }

    public boolean update(Procedure procedure) {
        return procedureDAO.update(procedure);
    }

    public boolean delete(int id) {
        return procedureDAO.delete(id);
    }

    public List<Procedure> getAll() {
        List<Procedure> listProcedure = new ArrayList<>();
        ProcedureSkillsBO procedureSkills = new ProcedureSkillsBO();
        Set<Procedure> setProcedure = procedureDAO.getAll();
        if (setProcedure == null) {
            return null;
        }
        for (Procedure procedure : setProcedure) {
            Set<Competency> setCompetency = procedureSkills.getAllCompetencies(procedure);
            if (setCompetency == null) {
                return null;
            }
            for (Competency competency : setCompetency) {
                procedure.addCompetency(competency);
            }
            listProcedure.add(procedure);
        }
        return listProcedure;
    }

    public Procedure get(int id) {
        Procedure procedure = procedureDAO.get(id);
        ProcedureSkillsBO procedureSkills = new ProcedureSkillsBO();
        procedureSkills.getAllCompetencies(procedure).forEach((competency) -> {
            procedure.addCompetency(competency);
        });
        return procedure;
    }

}
