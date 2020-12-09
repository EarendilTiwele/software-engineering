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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alexd
 */
public class PostgresMaintainerSkillsDAO
        extends PostgresAbstractDAO<Competency> implements MaintainerSkillsDAO {

    /**
     * Returns the <code>Competency</code> object builded on the current row of
     * the ResultSet <code>rs</code>.
     *
     * @param rs the ResultSet with which to build the <code>Competency</code>
     * object
     * @return the <code>Competency</code> object builded on the current row of
     * the ResultSet <code>rs</code>
     * @throws SQLException if a database access error occurs
     */
    @Override
    Competency convertToEntity(ResultSet rs) throws SQLException {
        Competency dbCompetency = new Competency(rs.getInt("id"),
                rs.getString("description"));
        return dbCompetency;
    }

    /**
     * Retrieves a <code>Set</code> of <code>Competency</code> objects
     * associated to a <code>maintainer</code> from the Postgres Database.
     * Returns the <code>Set</code> of <code>Competency</code> objects
     * associated to the <code>maintainer</code> if the operation is successful;
     * <code>null</code> otherwise.
     *
     * @param maintainer the maintainer with the associated competencies
     * @return the <code>Set</code> of <code>Competency</code> objects
     * associated to the <code>maintainer</code> if the operation is successful;
     * <code>null</code> otherwise
     */
    @Override
    public Set<Competency> getAllCompetencies(Maintainer maintainer) {
        String query = String.format("select * from competency where id in "
                + "(select competencyId from "
                + "maintainerhascompetencies where "
                + "maintainerId = %d);", maintainer.getId());
        try {
            return executeSetQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(PostgresMaintainerSkillsDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
