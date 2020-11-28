/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import businesslogiclayer.Procedure;
import java.util.List;

/**
 *
 * @author avall
 */
public interface ProcedureDAL {
    
    public Procedure insert(Procedure procedure);
    public Procedure update(Procedure procedure);
    public Procedure delete(int id);
    public List<Procedure> getAll();
    public Procedure get(int id);
    public List<Procedure> deleteAll();
}
