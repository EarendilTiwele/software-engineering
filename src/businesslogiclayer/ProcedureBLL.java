/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

import dataaccesslayer.ProcedureDAL;
import dataaccesslayer.ProcedureDALDatabase;
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
        List<Procedure> procedureList = new ArrayList<>();
        for (Procedure p : procedureDAL.getAll()) {
            procedureList.add(p);
        }
        return procedureList;
    }

    public Procedure get(int id) throws SQLException {
        return procedureDAL.get(id);
    }

}
