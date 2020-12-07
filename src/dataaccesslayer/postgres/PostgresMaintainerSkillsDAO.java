/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer.postgres;

import datatransferobjects.Competency;
import datatransferobjects.Maintainer;
import dataaccesslayer.MaintainerSkillsDAO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

/**
 *
 * @author alexd
 */
public class PostgresMaintainerSkillsDAO 
    extends PostgresAbstractDAO<Competency> implements MaintainerSkillsDAO {

    @Override
    public Competency convertToEntity(ResultSet rs) throws SQLException {
        Competency dbCompetency = new Competency(rs.getInt("id"),
                                                 rs.getString("description"));
        return dbCompetency;
    }
    
    /**
     * Retrieve all the competencies associated to a maintainer from a database
     * @param maintainer the maintainer with the associated competencies
     * @return the set of the competencies retrieved
     * @throws java.sql.SQLException
     */
    @Override
    public Set<Competency> getAllCompetencies(Maintainer maintainer) throws SQLException {
        String query = String.format("select * from competency where id in "
                                   + "(select competencyId from "
                                   + "maintainerhascompetencies where "
                                   + "maintainerId = %d);", maintainer.getId());
        return executeSetQuery(query);
    }
    
}
