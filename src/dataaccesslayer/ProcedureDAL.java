/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import businesslogiclayer.Procedure;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/**
 *
 * @author avall
 */
public interface ProcedureDAL {
    
    public Procedure insert(Procedure procedure) throws SQLException;
    public Procedure update(Procedure procedure) throws SQLException;
    public Procedure delete(int id) throws SQLException;
    public Set<Procedure> getAll() throws SQLException;
    public Procedure get(int id) throws SQLException;
    public Set<Procedure> deleteAll() throws SQLException;
}
