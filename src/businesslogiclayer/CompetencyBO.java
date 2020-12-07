/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

import datatransferobjects.Competency;
import dataaccesslayer.CompetencyDAO;
import dataaccesslayer.DAOFactory;
import java.sql.SQLException;

/**
 *
 * @author alexd
 */
public class CompetencyBO {
    
    private final CompetencyDAO competencyDAL;
    
    public CompetencyBO() {
        DAOFactory postgresFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
        competencyDAL = postgresFactory.getCompetencyDAO();
    }
    
    public Competency get(int id) throws SQLException {
        return competencyDAL.get(id);
    }
    
}
