/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import datatransferobjects.Procedure;
import java.util.Set;

/**
 *
 * @author avall
 */
public interface ProcedureDAO {

    /**
     * Inserts an <code>procedure</code> in the database
     *
     * @param procedure
     * @return the id of procedure if the procedure is inserted correctly;
     * otherwise - 1 if the insert operation is failed
     */
    public int insert(Procedure procedure);

    /**
     * Updates an <code>procedure</code> in the database
     *
     * @param procedure
     * @return true if the procedure has been updated or if there was not the
     * procedure; otherwise false in case of error.
     */
    public boolean update(Procedure procedure);

    /**
     * Deletes an <code>procedure</code> in the database
     *
     * @param id
     * @return true if the procedure has been deleted or if there was not the
     * procedure in the database; otherwise false in case of error.
     */
    public boolean delete(int id);

    /**
     * Returns the Set of the procedures present in the database
     *
     * @return the set of the procedures; otherwise null in case of error.
     */
    public Set<Procedure> getAll();

    /**
     * Returns a procedure with the specified <code>id</code>
     *
     * @param id
     * @return a procedure with the specified <code>id</code>
     */
    public Procedure get(int id);
}
