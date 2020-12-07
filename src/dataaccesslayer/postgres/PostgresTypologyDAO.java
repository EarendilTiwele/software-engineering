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

/**
 *
 * @author alexd
 */
public class PostgresTypologyDAO extends PostgresAbstractDAO<Typology> implements TypologyDAO {
    
    @Override
    public Typology convertToEntity(ResultSet rs) throws SQLException {
        Typology dbTypology = new Typology(rs.getInt("id"), rs.getString("typo"));
        return dbTypology;
    }

    /**
     * Insert a typology in a database
     * @param typology the typology to insert
     * @return the inserted typology
     * @throws java.sql.SQLException
     */
    @Override
    public Typology insert(Typology typology) throws SQLException {
        String query = String.format("insert into typology "
                                   + "(typo) values ('%s') returning *;"
                                   , typology.getName());
        return executeQuery(query);
    }
    
    /**
     * Update a typology in a database
     * @param typology the typology to update
     * @return the updated typology
     * @throws java.sql.SQLException
     */
    @Override
    public Typology update(Typology typology) throws SQLException {
        String query = String.format("update typology "
                                   + "set typo = '%s' "
                                   + "where id = %d returning *;"
                                   , typology.getName(), typology.getId());
        return executeQuery(query);
    }

    /**
     * Delete a typology with given id from a database
     * @param id the id which identifies the typology
     * @return the deleted typology
     * @throws java.sql.SQLException
     */
    @Override
    public Typology delete(int id) throws SQLException {
        String query = String.format("delete from typology "
                                   + "where id = %d returning *;", id);
        return executeQuery(query);
    }

    /**
     * Delete all typologies from a database
     * @return the set of deleted typologies
     * @throws java.sql.SQLException
     */
    @Override
    public Set<Typology> deleteAll() throws SQLException {
        String query = "delete from typology returning *;";
        return executeSetQuery(query);
    }

    /** 
     * Retrieve a typology with given id from a database
     * @param id the id which identifies the typology
     * @return the typology retrieved
     * @throws java.sql.SQLException
     */
    @Override
    public Typology get(int id) throws SQLException {
        String query = String.format("select * from typology "
                                   + "where id = %d;", id);
        return executeQuery(query);
    }

    /**
     * Retrieve all the typologies from a database
     * @return the set of the typologies retrieved
     * @throws java.sql.SQLException
     */
    @Override
    public Set<Typology> getAll() throws SQLException {
        String query = "select * from typology;";
        return executeSetQuery(query);
    }
    
}
