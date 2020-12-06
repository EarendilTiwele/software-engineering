/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import businesslogiclayer.Typology;
import java.sql.SQLException;
import java.util.Set;

/**
 *
 * @author alexd
 */
public interface TypologyDAL {
    
    /**
     * Insert a typology in a persistent storage
     * @param typology the typology to insert
     * @return the inserted typology
     * @throws java.sql.SQLException
     */
    public Typology insert (Typology typology) throws SQLException;
    
    /**
     * Update a typology in a persistent storage
     * @param typology the typology to update
     * @return the updated typology
     * @throws java.sql.SQLException
     */
    public Typology update (Typology typology) throws SQLException;
    
    /**
     * Delete a typology with given id from a persistent storage
     * @param id the id which identifies the typology
     * @return the deleted typology
     * @throws java.sql.SQLException
     */
    public Typology delete (int id) throws SQLException;
    
    /**
     * Delete all typologies in a persistent storage
     * @return the set of deleted typologies
     * @throws java.sql.SQLException
     */
    public Set<Typology> deleteAll () throws SQLException;
    
    /** 
     * Retrieve a typology with given id from a persistent storage
     * @param id the id which identifies the typology
     * @return the typology retrieved
     * @throws java.sql.SQLException
     */
    public Typology get(int id) throws SQLException;
    
    /**
     * Retrieve all the typologies from a persistent storage
     * @return the set of the typologies retrieved
     * @throws java.sql.SQLException
     */
    public Set<Typology> getAll() throws SQLException;
    
}
