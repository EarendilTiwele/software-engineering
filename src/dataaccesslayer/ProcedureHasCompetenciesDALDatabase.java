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
public class ProcedureHasCompetenciesDALDatabase implements ProcedureHasCompetenciesDAL {

    private Connection conn;
    
    /**
     * Get all competencies associated with a specific procedure
     * @param procedure
     * @return the competencies of the procedure
     */
    @Override
    public Set<Competency> getAllCompetencies(Procedure procedure) {
        boolean connectionWasClosed = DatabaseConnection.isClosed();
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement prepareStatement
                    = conn.prepareStatement("select tb1.id as ProcedureId, tb1.name as ProcedureName, tb1.smp as ProcedureSmp,\n" +
                    "competency.id as CompetencyId, competency.description as CompetencyDescription " +
                    "from (procedure inner join procedurehascompetencies on " +
                    "procedure.id = procedurehascompetencies.procedureid) as tb1 " +
                    "inner join competency on tb1.competencyid = competency.id "+
                    "where ProcedureId = ?; ");
            prepareStatement.setInt(1, procedure.getId());
            ResultSet rs = prepareStatement.executeQuery();
            Competency competency = null;
            Set<Competency> competencySet = new HashSet<>();
            while (rs.next()) {
                competency = new Competency(rs.getInt("CompetencyId"), rs.getString("CompetencyDescription"));
                competencySet.add(competency);
            }
            if (connectionWasClosed) {
                conn.close();
            }
            return competencySet;
        } catch (SQLException ex) {
            Logger.getLogger(SiteDALDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
