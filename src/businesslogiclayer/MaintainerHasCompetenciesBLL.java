/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

import dataaccesslayer.MaintainerHasCompetenciesDALDatabase;
import java.sql.SQLException;
import java.util.Set;

/**
 *
 * @author alexd
 */
public class MaintainerHasCompetenciesBLL {
    
    private final MaintainerHasCompetenciesDALDatabase mhcDAL;
    
    public MaintainerHasCompetenciesBLL() {
        mhcDAL = new MaintainerHasCompetenciesDALDatabase();
    }
    
    public Set<Competency> getAllCompetencies(Maintainer maintainer) throws SQLException {
        return mhcDAL.getAllCompetencies(maintainer);
    }
    
}
