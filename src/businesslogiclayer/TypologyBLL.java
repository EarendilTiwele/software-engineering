/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

import dataaccesslayer.TypologyDAL;
import dataaccesslayer.TypologyDALDatabase;
import java.util.List;

/**
 *
 * @author alexd
 */
public class TypologyBLL {
    
    private final TypologyDAL typologyDAL;

    public TypologyBLL() {
        typologyDAL = new TypologyDALDatabase();
    }

    public Typology insert(Typology typology) {
        return typologyDAL.insert(typology);
    }

    public Typology update(Typology typology) {
        return typologyDAL.update(typology);
    }

    public Typology delete(int id) {
        return typologyDAL.delete(id);
    }

    public Typology get(int id) {
        return typologyDAL.get(id);
    }
    
    public List<Typology> getAll() {
        return typologyDAL.getAll();
    }
    
}
