/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer.postgres;

import datatransferobjects.Competency;
import dataaccesslayer.CompetencyDAO;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author alexd
 */
public class PostgresCompetencyDAO extends PostgresAbstractDAO<Competency> implements CompetencyDAO {
    
    @Override
    public Competency convertToEntity(ResultSet rs) throws SQLException {
        Competency dbCompetency = new Competency(rs.getInt("id"),
                                      rs.getString("description"));
        return dbCompetency;
    }
    
    /** 
     * Retrieve a competency with given id from a database
     * @param id the id which identifies the competency
     * @return the competency retrieved
     * @throws java.sql.SQLException
     */
    @Override
    public Competency get(int id) throws SQLException {
        String query = String.format("select * from competency "
                                   + "where id = %d;", id);
        return executeQuery(query);
    }
    
}
