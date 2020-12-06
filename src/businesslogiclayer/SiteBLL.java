/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

import dataaccesslayer.SiteDAL;
import dataaccesslayer.SiteDALDatabase;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author avall
 */
public class SiteBLL {

    private final SiteDAL siteDAL;

    public SiteBLL() {
        siteDAL = new SiteDALDatabase();
    }

    public Site insert(Site site) throws SQLException {
        return siteDAL.insert(site);
    }
    
    public Site update (Site site) throws SQLException {
        return siteDAL.update(site);
    }
    
    public Site delete(int id) throws SQLException {
        return siteDAL.delete(id);
    }
    
    public List<Site> getAll() throws SQLException {
        return new ArrayList<>(siteDAL.getAll());
    }
    
    public Site get(int id) throws SQLException {
        return siteDAL.get(id);
    }
}
