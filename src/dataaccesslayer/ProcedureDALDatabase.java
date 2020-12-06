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
import java.util.Set;

/**
 *
 * @author avall
 */
public class ProcedureDALDatabase extends AbstractDAL<Procedure> implements ProcedureDAL {


    @Override
    public Procedure convertToEntity(ResultSet rs) throws SQLException {
        Procedure procedure = new Procedure(rs.getInt("id"), rs.getString("name"), rs.getString("smp"));
//        PreparedStatement prepareStatement
//                    = DatabaseConnection.getConnection().prepareStatement("select tb1.id as ProcedureId, tb1.name as ProcedureName, tb1.smp as ProcedureSmp, " +
//                    "competency.id as CompetencyId, competency.description as CompetencyDescription " +
//                    "from (procedure inner join procedurehascompetencies on " +
//                    "procedure.id = procedurehascompetencies.procedureid) as tb1 " +
//                    "inner join competency on tb1.competencyid = competency.id "+
//                    "where ProcedureId = ?; ");
//            prepareStatement.setInt(1, procedure.getId());
//            ResultSet rs2 = prepareStatement.executeQuery();
//            Competency competency = null;
//            while (rs2.next()) {
//                competency = new Competency(rs.getInt("CompetencyId"), rs.getString("CompetencyDescription"));
//                procedure.addCompetency(competency);
//            }
         return procedure;
    }

    /**
     * Insert a specific procedure in the database
     *
     * @param procedure
     * @return the procedure inserted from the database
     * @throws java.sql.SQLException
     */
    @Override
    public Procedure insert(Procedure procedure) throws SQLException {
        String query = String.format("insert into Procedure (name, smp) values ('%s','%s') returning *;",
                procedure.getName(), procedure.getSmp());
        return executeQuery(query);
    }

    /**
     * Update a specific procedure in the database
     *
     * @param procedure
     * @return the procedure updated from the database
     * @throws java.sql.SQLException
     */
    @Override
    public Procedure update(Procedure procedure) throws SQLException {
        String query = String.format("update procedure "
                + "set name = '%s', smp = '%s' "
                + "where id = %d returning *;", procedure.getName(),
                procedure.getSmp(), procedure.getId());
        return executeQuery(query);
    }

    /**
     * Delete a procedure with a specific id
     *
     * @param id
     * @return the procedure deleted from the database
     * @throws java.sql.SQLException
     */
    @Override
    public Procedure delete(int id) throws SQLException {
        String query = String.format("delete from procedure "
                + "where id = %d returning *;", id);
        return executeQuery(query);
    }

    /**
     * Get all procedures from the database
     *
     * @return all procedures from the database
     * @throws java.sql.SQLException
     */
    @Override
    public Set<Procedure> getAll() throws SQLException {
        String query = String.format("select * from procedure;");
        return executeSetQuery(query);
    }

    /**
     * Get a specific procedure from the database
     *
     * @param id
     * @return the procedure specified
     */
    @Override
    public Procedure get(int id) throws SQLException {
        String query = String.format("select * from Procedure "
                            + "where id = %d;",id);
        return executeQuery(query);
    }

    /**
     * Delete all procedures from the database
     *
     * @return the procedures deleted from the database
     */
    @Override
    public Set<Procedure> deleteAll() throws SQLException {
        String query = String.format("delete from procedure returning *;");
        return executeSetQuery(query);
    }

}
