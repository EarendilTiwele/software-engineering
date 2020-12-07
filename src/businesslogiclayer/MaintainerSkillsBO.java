/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

import datatransferobjects.Competency;
import datatransferobjects.Maintainer;
import dataaccesslayer.DAOFactory;
import dataaccesslayer.MaintainerSkillsDAO;
import java.sql.SQLException;
import java.util.Set;

/**
 *
 * @author alexd
 */
public class MaintainerSkillsBO {
    
    private final MaintainerSkillsDAO maintainerSkillsDAO;
    
    public MaintainerSkillsBO() {
        DAOFactory postgresFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
        maintainerSkillsDAO = postgresFactory.getMaintainerSkillsDAO();
    }
    
    public Set<Competency> getAllCompetencies(Maintainer maintainer) throws SQLException {
        return maintainerSkillsDAO.getAllCompetencies(maintainer);
    }
    
}
