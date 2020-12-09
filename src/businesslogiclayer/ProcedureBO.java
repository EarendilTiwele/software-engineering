/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

import datatransferobjects.Procedure;
import dataaccesslayer.DAOFactory;
import dataaccesslayer.postgres.PostgresProcedureSkillsDAO;
import java.util.ArrayList;
import java.util.List;
import dataaccesslayer.ProcedureDAO;
import dataaccesslayer.ProcedureSkillsDAO;

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
        ProcedureSkillsDAO procedureHasCompetencies = new PostgresProcedureSkillsDAO();
        procedureDAO.getAll().stream().map((procedure) -> {
            procedureHasCompetencies.getAllCompetencies(procedure).forEach((competency) -> {
                procedure.addCompetency(competency);
            });
            return procedure;
        }).forEachOrdered((procedure) -> {
            listProcedure.add(procedure);
        });
        return listProcedure;
    }

    public Procedure get(int id) {
        Procedure procedure = procedureDAO.get(id);
        ProcedureSkillsDAO procedureHasCompetencies = new PostgresProcedureSkillsDAO();
        procedureHasCompetencies.getAllCompetencies(procedure).forEach((competency) -> {
            procedure.addCompetency(competency);
        });
        return procedure;
    }

}
