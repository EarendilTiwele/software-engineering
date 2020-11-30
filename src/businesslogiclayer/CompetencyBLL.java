/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

import dataaccesslayer.CompetencyDALDatabase;
import java.sql.SQLException;

/**
 *
 * @author alexd
 */
public class CompetencyBLL {
    
    private final CompetencyDALDatabase competencyDAL;
    
    public CompetencyBLL() {
        competencyDAL = new CompetencyDALDatabase();
    }
    
    public Competency get(int id) throws SQLException {
        return competencyDAL.get(id);
    }
    
}
