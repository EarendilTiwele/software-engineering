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
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alexd
 */
public class PostgresCompetencyDAO extends PostgresAbstractDAO<Competency> implements CompetencyDAO {

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
     * Inserts a <code>Competency</code> object in the Postgres Database.
     * Returns the id of the inserted <code>competency</code> if the operation
     * is successful; -1 otherwise.
     *
     * @param competency the <code>Competency</code> object to insert
     * @return the id of the inserted <code>competency</code> if the operation
     * is successful; -1 otherwise
     */
    @Override
    public int insert(Competency competency) {
        String query = String.format("insert into competency "
                + "(description)"
                + "values ('%s') returning *;",
                competency.getDescription());
        try {
            return executeQuery(query).getId();
        } catch (SQLException ex) {
            Logger.getLogger(PostgresCompetencyDAO.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    /**
     * Updates a <code>Competency</code> object in the Postgres Database, if the
     * <code>competency</code> is not present in the Postgres Database it is not
     * created. Returns <code>true</code> if the operation is successful, that
     * is both when the <code>competency</code> is updated and when the
     * <code>competency</code> doesn't exist in the Postgres Database;
     * <code>false</code> otherwise.
     *
     * @param competency the <code>Competency</code> object to update
     * @return <code>true</code> if the operation is successful, that is both
     * when the competency is updated and when the competency doesn't exist in
     * the Postgres Database; <code>false</code> otherwise
     */
    @Override
    public boolean update(Competency competency) {
        String query = String.format("update competency "
                + "set description = '%s' "
                + "where id = %d;",
                competency.getDescription(), competency.getId());
        try {
            executeUpdate(query);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(PostgresCompetencyDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Retrieves the <code>Competency</code> object with given <code>id</code>
     * from the Postgres Database. Returns the <code>Competency</code> object
     * with given <code>id</code> if it exists in the Postgres Database;
     * <code>null</code> if the <code>Competency</code> object with given
     * <code>id</code> doesn't exist in the Postgres Database or if the
     * operation fails.
     *
     * @param id the id which identifies the competency
     * @return the <code>Competency</code> object with given <code>id</code> if
     * it exists in the Postgres Database, returns <code>null</code> if the
     * <code>Competency</code> object with given <code>id</code> doesn't exist
     * in the Postgres Database or if the operation fails
     */
    @Override
    public Competency get(int id) {
        String query = String.format("select * from competency "
                + "where id = %d;", id);
        try {
            return executeQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(PostgresCompetencyDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Retrieves a <code>Set</code> of <code>Competency</code> objects from the
     * Postgres Database. Returns the <code>Set</code> of
     * <code>Competency</code> objects if the operation is successful;
     * <code>null</code> otherwise.
     *
     * @return the <code>Set</code> of <code>Competency</code> objects from the
     * Postgres Database if the operation is successful; <code>null</code>
     * otherwise
     */
    @Override
    public Set<Competency> getAll() {
        String query = "select * from competency;";
        try {
            return executeSetQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(PostgresCompetencyDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
