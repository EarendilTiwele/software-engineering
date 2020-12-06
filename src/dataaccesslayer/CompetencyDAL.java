/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import businesslogiclayer.Competency;
import java.sql.SQLException;

/**
 *
 * @author alexd
 */
public interface CompetencyDAL {
    
    /** 
     * Retrieve a competency with given id from a persistent storage
     * @param id the id which identifies the competency
     * @return the competency retrieved
     * @throws java.sql.SQLException
     */
    public Competency get(int id) throws SQLException;
    
}
