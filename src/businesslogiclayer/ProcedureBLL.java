/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

import dataaccesslayer.ProcedureDAL;
import dataaccesslayer.ProcedureDALDatabase;
import dataaccesslayer.ProcedureHasCompetenciesDAL;
import dataaccesslayer.ProcedureHasCompetenciesDALDatabase;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author avall
 */
public class ProcedureBLL {

    private ProcedureDAL procedureDAL;

    public ProcedureBLL() {
        procedureDAL = new ProcedureDALDatabase();
    }

    public Procedure insert(Procedure procedure) throws SQLException {
        return procedureDAL.insert(procedure);
    }

    public Procedure update(Procedure procedure) throws SQLException {
        return procedureDAL.update(procedure);
    }

    public Procedure delete(int id) throws SQLException {
        return procedureDAL.delete(id);
    }

    public List<Procedure> getAll() throws SQLException {
        List<Procedure> listProcedure = new ArrayList<>();
        ProcedureHasCompetenciesDAL procedureHasCompetencies = new ProcedureHasCompetenciesDALDatabase();
        for (Procedure procedure : procedureDAL.getAll()) {
            for (Competency competency : procedureHasCompetencies.getAllCompetencies(procedure)) {
                procedure.addCompetency(competency);
            }
            listProcedure.add(procedure);
        }
        return listProcedure;
    }

    public Procedure get(int id) throws SQLException {
        Procedure procedure = procedureDAL.get(id);
        ProcedureHasCompetenciesDAL procedureHasCompetencies = new ProcedureHasCompetenciesDALDatabase();
        for (Competency competency : procedureHasCompetencies.getAllCompetencies(procedure)) {
            procedure.addCompetency(competency);
        }
        return procedure;
    }

}
