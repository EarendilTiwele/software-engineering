/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import businesslogiclayer.Competency;
import businesslogiclayer.Procedure;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author avall
 */
public class ProcedureHasCompetenciesDALDatabase extends AbstractDAL<Competency> implements ProcedureHasCompetenciesDAL {


    @Override
    public Competency convertToEntity(ResultSet rs) throws SQLException {
        return new Competency(rs.getInt("CompetencyId"), rs.getString("CompetencyDescription"));
    }

    /**
     * Get all competencies associated with a specific procedure
     *
     * @param procedure
     * @return the competencies of the procedure
     */
    @Override
    public Set<Competency> getAllCompetencies(Procedure procedure) throws SQLException {
        String query = String.format("select tb1.id as ProcedureId, tb1.name as ProcedureName, tb1.smp as ProcedureSmp,\n"
                + "competency.id as CompetencyId, competency.description as CompetencyDescription "
                + "from (procedure inner join procedurehascompetencies on "
                + "procedure.id = procedurehascompetencies.procedureid) as tb1 "
                + "inner join competency on tb1.competencyid = competency.id "
                + "where ProcedureId = %d; ", procedure.getId());
        return executeSetQuery(query);
    }

}
