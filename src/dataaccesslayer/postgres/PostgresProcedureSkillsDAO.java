/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer.postgres;

import datatransferobjects.Competency;
import datatransferobjects.Procedure;
import dataaccesslayer.ProcedureSkillsDAO;
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
public class PostgresProcedureSkillsDAO extends PostgresAbstractDAO<Competency> implements ProcedureSkillsDAO {

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
    public Set<Competency> getAllCompetencies(Procedure procedure) {
        String query = String.format("select tb1.id as ProcedureId, tb1.name as ProcedureName, tb1.smp as ProcedureSmp,\n"
                + "competency.id as CompetencyId, competency.description as CompetencyDescription "
                + "from (procedure inner join procedurehascompetencies on "
                + "procedure.id = procedurehascompetencies.procedureid) as tb1 "
                + "inner join competency on tb1.competencyid = competency.id "
                + "where ProcedureId = %d; ", procedure.getId());
        try {
            return executeSetQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(PostgresProcedureSkillsDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
