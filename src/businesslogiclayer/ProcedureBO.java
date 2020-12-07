/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

import datatransferobjects.Competency;
import datatransferobjects.Procedure;
import dataaccesslayer.DAOFactory;
import dataaccesslayer.postgres.PostgresProcedureSkillsDAO;
import java.sql.SQLException;
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

    public Procedure insert(Procedure procedure) throws SQLException {
        return procedureDAO.insert(procedure);
    }

    public Procedure update(Procedure procedure) throws SQLException {
        return procedureDAO.update(procedure);
    }

    public Procedure delete(int id) throws SQLException {
        return procedureDAO.delete(id);
    }

    public List<Procedure> getAll() throws SQLException {
        List<Procedure> listProcedure = new ArrayList<>();
        ProcedureSkillsDAO procedureHasCompetencies = new PostgresProcedureSkillsDAO();
        for (Procedure procedure : procedureDAO.getAll()) {
            for (Competency competency : procedureHasCompetencies.getAllCompetencies(procedure)) {
                procedure.addCompetency(competency);
            }
            listProcedure.add(procedure);
        }
        return listProcedure;
    }

    public Procedure get(int id) throws SQLException {
        Procedure procedure = procedureDAO.get(id);
        ProcedureSkillsDAO procedureHasCompetencies = new PostgresProcedureSkillsDAO();
        for (Competency competency : procedureHasCompetencies.getAllCompetencies(procedure)) {
            procedure.addCompetency(competency);
        }
        return procedure;
    }

}
