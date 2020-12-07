/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

import datatransferobjects.Site;
import dataaccesslayer.DAOFactory;
import dataaccesslayer.postgres.PostgresSiteDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import dataaccesslayer.SiteDAO;

/**
 *
 * @author avall
 */
public class SiteBO {

    private final SiteDAO siteDAO;

    public SiteBO() {
        DAOFactory postgresFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
        siteDAO = postgresFactory.getSiteDAO();
    }

    public Site insert(Site site) throws SQLException {
        return siteDAO.insert(site);
    }
    
    public Site update (Site site) throws SQLException {
        return siteDAO.update(site);
    }
    
    public Site delete(int id) throws SQLException {
        return siteDAO.delete(id);
    }
    
    public List<Site> getAll() throws SQLException {
        return new ArrayList<>(siteDAO.getAll());
    }
    
    public Site get(int id) throws SQLException {
        return siteDAO.get(id);
    }
}
