/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer.postgres;

import datatransferobjects.Typology;
import dataaccesslayer.TypologyDAO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alexd
 */
public class PostgresTypologyDAO extends PostgresAbstractDAO<Typology> implements TypologyDAO {

    /**
     * Returns the <code>Typology</code> object builded on the current row of
     * the ResultSet <code>rs</code>.
     *
     * @param rs the ResultSet with which to build the <code>Typology</code>
     * object
     * @return the <code>Typology</code> object builded on the current row of
     * the ResultSet <code>rs</code>
     * @throws SQLException if a database access error occurs
     */
    @Override
    Typology convertToEntity(ResultSet rs) throws SQLException {
        Typology dbTypology = new Typology(rs.getInt("id"), rs.getString("typo"));
        return dbTypology;
    }

    /**
     * Inserts a <code>Typology</code> object in the Postgres Database. Returns
     * the id of the inserted <code>typology</code> if the operation is
     * successful; -1 otherwise.
     *
     * @param typology the <code>Typology</code> object to insert
     * @return the id of the inserted <code>typology</code> if the operation is
     * successful; -1 otherwise
     */
    @Override
    public int insert(Typology typology) {
        String query = String.format("insert into typology "
                + "(typo) values ('%s') returning *;",
                typology.getName());
        try {
            return executeQuery(query).getId();
        } catch (SQLException ex) {
            Logger.getLogger(PostgresTypologyDAO.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    /**
     * Updates a <code>Typology</code> object in the Postgres Database, if the
     * <code>typology</code> is not present in the Postgres Database it is not
     * created. Returns <code>true</code> if the operation is successful, that
     * is both when the <code>typology</code> is updated and when the
     * <code>typology</code> doesn't exist in the Postgres Database;
     * <code>false</code> otherwise.
     *
     * @param typology the <code>Typology</code> object to update
     * @return <code>true</code> if the operation is successful, that is both
     * when the typology is updated and when the typology doesn't exist in the
     * Postgres Database; <code>false</code> otherwise
     */
    @Override
    public boolean update(Typology typology) {
        String query = String.format("update typology "
                + "set typo = '%s' "
                + "where id = %d;",
                typology.getName(), typology.getId());
        try {
            executeUpdate(query);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(PostgresTypologyDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Deletes the typology with given <code>id</code> from the Postgres
     * Database. Returns <code>true</code> if the operation is successful, that
     * is both when the typology with given <code>id</code> is deleted and when
     * the typology with given <code>id</code> doesn't exist in the Postgres
     * Database; <code>false</code> otherwise.
     *
     * @param id the <code>id</code> which identifies the typology
     * @return <code>true</code> if the operation is successful, that is both
     * when the typology with given <code>id</code> is deleted and when the
     * typology with given <code>id</code> doesn't exist in the Postgres
     * Database; <code>false</code> otherwise
     */
    @Override
    public boolean delete(int id) {
        String query = String.format("delete from typology "
                + "where id = %d;", id);
        try {
            executeUpdate(query);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(PostgresTypologyDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Retrieves the <code>Typology</code> object with given <code>id</code>
     * from the Postgres Database. Returns the <code>Typology</code> object with
     * given <code>id</code> if it exists in the Postgres Database;
     * <code>null</code> if the <code>Typology</code> object with given
     * <code>id</code> doesn't exist in the Postgres Database or if the
     * operation fails.
     *
     * @param id the id which identifies the site
     * @return the <code>Typology</code> object with given <code>id</code> if it
     * exists in the Postgres Database, returns <code>null</code> if the
     * <code>Typology</code> object with given <code>id</code> doesn't exist in
     * the Postgres Database or if the operation fails
     */
    @Override
    public Typology get(int id) {
        String query = String.format("select * from typology "
                + "where id = %d;", id);
        try {
            return executeQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(PostgresTypologyDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Retrieves a <code>Set</code> of <code>Typology</code> objects from the
     * Postgres Database. Returns the <code>Set</code> of <code>Typology</code>
     * objects if the operation is successful; <code>null</code> otherwise.
     *
     * @return the <code>Set</code> of <code>Typology</code> objects from the
     * Postgres Database if the operation is successful; <code>null</code>
     * otherwise
     */
    @Override
    public Set<Typology> getAll() {
        String query = "select * from typology;";
        try {
            return executeSetQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(PostgresTypologyDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
