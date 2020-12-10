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
