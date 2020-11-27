/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import businesslogiclayer.Typology;
import java.util.List;

/**
 *
 * @author alexd
 */
public interface TypologyDAL {
    
    /**
     * Insert a typology in a persistent storage
     * @param typology the typology to insert
     * @return the inserted typology
     */
    public Typology insert (Typology typology);
    
    /**
     * Update a typology in a persistent storage
     * @param typology the typology to update
     * @return the updated typology
     */
    public Typology update (Typology typology);
    
    /**
     * Delete a typology with given id from a persistent storage
     * @param id the id which identifies the typology
     * @return the deleted typology
     */
    public Typology delete (int id);
    
    /**
     * Delete all typologies in a persistent storage
     * @return the number of deleted typologies
     */
    public int deleteAll ();
    
    /** 
     * Retrieve a typology with given id from a persistent storage
     * @param id the id which identifies the typology
     * @return the typology retrieved
     */
    public Typology get(int id);
    
    /**
     * Retrieve all the typologies from a persistent storage
     * @return the list of the typologies retrieved
     */
    public List<Typology> getAll();
    
}
