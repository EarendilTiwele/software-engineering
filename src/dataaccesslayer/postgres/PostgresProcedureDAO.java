/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer.postgres;

import datatransferobjects.Procedure;
import dataaccesslayer.ProcedureDAO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author avall
 */
public class PostgresProcedureDAO extends PostgresAbstractDAO<Procedure> implements ProcedureDAO {

    /**
     * Returns the <code>Procedure</code> object builded on the current row of
     * the ResultSet <code>rs</code>.
     *
     * @param rs the ResultSet with which to build the <code>Procedure</code>
     * object
     * @return the <code>Procedure</code> object builded on the current row of
     * the ResultSet <code>rs</code>
     * @throws SQLException if a database access error occurs
     */
    @Override
    public Procedure convertToEntity(ResultSet rs) throws SQLException {
        Procedure procedure = new Procedure(rs.getInt("id"), rs.getString("name"), rs.getString("smp"));
        return procedure;
    }

    /**
     * Inserts a <code>Procedure</code> object in a database. Returns the id of
     * the inserted <code>procedure</code> if the operation is successful; -1
     * otherwise.
     *
     * @param procedure the <code>Procedure</code> object to insert
     * @return the id of the inserted <code>procedure</code> if the operation is
     * successful; -1 otherwise
     */
    @Override
    public int insert(Procedure procedure) {
        String query = String.format("insert into Procedure (name, smp) values ('%s','%s') returning *;",
                procedure.getName(), procedure.getSmp());
        try {
            return executeQuery(query).getId();
        } catch (SQLException ex) {
            Logger.getLogger(PostgresProcedureDAO.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    /**
     * Updates a <code>Procedure</code> object in a database, if the
     * <code>procedure</code> is not present in the database it is not created.
     * Returns <code>true</code> if the operation is successful, that is both
     * when the <code>procedure</code> is updated and when the
     * <code>procedure</code> doesn't exist in the database; <code>false</code>
     * otherwise.
     *
     * @param procedure the <code>Procedure</code> object to update
     * @return <code>true</code> if the operation is successful, that is both
     * when the procedure is updated and when the procedure doesn't exist in the
     * database; <code>false</code> otherwise
     */
    @Override
    public boolean update(Procedure procedure) {
        String query = String.format("update procedure "
                + "set name = '%s', smp = '%s' "
                + "where id = %d", procedure.getName(),
                procedure.getSmp(), procedure.getId());
        try {
            executeUpdate(query);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(PostgresProcedureDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Deletes the procedure with given <code>id</code> from a database. Returns
     * <code>true</code> if the operation is successful, that is both when the
     * procedure with given <code>id</code> is deleted and when the procedure
     * with given <code>id</code> doesn't exist in the database;
     * <code>false</code> otherwise.
     *
     * @param id the <code>id</code> which identifies the procedure
     * @return <code>true</code> if the operation is successful, that is both
     * when the procedure with given <code>id</code> is deleted and when the
     * procedure with given <code>id</code> doesn't exist in the database;
     * <code>false</code> otherwise
     */
    @Override
    public boolean delete(int id) {
        String query = String.format("delete from procedure "
                + "where id = %d;", id);
        try {
            executeUpdate(query);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(PostgresProcedureDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Retrieves a <code>Set</code> of <code>Procedure</code> objects from a
     * database. Returns the <code>Set</code> of <code>Procedure</code> objects
     * if the operation is successful; <code>null</code> otherwise.
     *
     * @return the <code>Set</code> of <code>Procedure</code> objects from the
     * database if the operation is successful; <code>null</code> otherwise
     */
    @Override
    public Set<Procedure> getAll() {
        String query = String.format("select * from procedure;");
        try {
            return executeSetQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(PostgresProcedureDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Retrieves the <code>Procedure</code> object with given <code>id</code>
     * from a database. Returns the <code>Procedure</code> object with given
     * <code>id</code> if it exists in the database; <code>null</code> if the
     * <code>Procedure</code> object with given <code>id</code> doesn't exist in
     * the database or if the operation fails.
     *
     * @param id the id which identifies the procedure
     * @return the <code>Procedure</code> object with given <code>id</code> if
     * it exists in the database, returns <code>null</code> if the
     * <code>Procedure</code> object with given <code>id</code> doesn't exist in
     * the database or if the operation fails
     */
    public Procedure get(int id) {
        String query = String.format("select * from Procedure "
                + "where id = %d;", id);
        try {
            return executeQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(PostgresProcedureDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
