/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

import dataaccesslayer.TypologyDAL;
import dataaccesslayer.TypologyDALDatabase;
import java.sql.SQLException;
import java.util.Set;

/**
 *
 * @author alexd
 */
public class TypologyBLL {
    
    private final TypologyDAL typologyDAL;

    public TypologyBLL() {
        typologyDAL = new TypologyDALDatabase();
    }

    public Typology insert(Typology typology) throws SQLException {
        return typologyDAL.insert(typology);
    }

    public Typology update(Typology typology) throws SQLException {
        return typologyDAL.update(typology);
    }

    public Typology delete(int id) throws SQLException {
        return typologyDAL.delete(id);
    }

    public Typology get(int id) throws SQLException {
        return typologyDAL.get(id);
    }
    
    public Set<Typology> getAll() throws SQLException {
        return typologyDAL.getAll();
    }
    
}
