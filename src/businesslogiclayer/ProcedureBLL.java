/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

import dataaccesslayer.ProcedureDAL;
import dataaccesslayer.ProcedureDALDatabase;
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

    public Procedure insert(Procedure procedure) {
        return procedureDAL.insert(procedure);
    }

    public Procedure update(Procedure procedure) {
        return procedureDAL.update(procedure);
    }

    public Procedure delete(int id) {
        return procedureDAL.delete(id);
    }

    public List<Procedure> getAll() {
        return procedureDAL.getAll();
    }

    public Procedure get(int id) {
        return procedureDAL.get(id);
    }

}
