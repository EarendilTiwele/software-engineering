/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

import dataaccesslayer.SiteDAL;
import dataaccesslayer.SiteDALDatabase;
import java.util.List;

/**
 *
 * @author avall
 */
public class SiteBLL {

    private SiteDAL siteDAL;

    public SiteBLL() {
        siteDAL = new SiteDALDatabase();
    }

    public Site insert(Site site) {
        return siteDAL.insert(site);
    }
    
    public Site update (Site site){
        return siteDAL.update(site);
    }
    
    public Site delete(int id){
        return siteDAL.delete(id);
    }
    
    public List<Site> getAll(){
        return siteDAL.getAll();
    }
    
    public Site get(int id){
        return siteDAL.get(id);
    }
}
