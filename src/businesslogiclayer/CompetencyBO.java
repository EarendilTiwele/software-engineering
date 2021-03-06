/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

import datatransferobjects.Competency;
import dataaccesslayer.CompetencyDAO;
import dataaccesslayer.DAOFactory;
import java.util.ArrayList;
import java.util.Set;
import java.util.List;

/**
 *
 * @author alexd
 */
public class CompetencyBO {

    private final CompetencyDAO competencyDAO;

    public CompetencyBO() {
        DAOFactory postgresFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
        competencyDAO = postgresFactory.getCompetencyDAO();
    }

    /**
     * Inserts a <code>Competency</code> object in a persistent storage. Returns
     * the id of the inserted <code>competency</code> if the operation is
     * successful; -1 otherwise.
     *
     * @param competency the <code>Competency</code> object to insert
     * @return the id of the inserted <code>competency</code> if the operation
     * is successful; -1 otherwise
     */
    public int insert(Competency competency) {
        return competencyDAO.insert(competency);
    }

    /**
     * Updates a <code>Competency</code> object in a persistent storage, if the
     * <code>competency</code> is not present in the persistent storage it is
     * not created. Returns <code>true</code> if the operation is successful,
     * that is both when the <code>competency</code> is updated and when the
     * <code>competency</code> doesn't exist in the persistent storage;
     * <code>false</code> otherwise.
     *
     * @param competency the <code>Competency</code> object to update
     * @return <code>true</code> if the operation is successful, that is both
     * when the competency is updated and when the competency doesn't exist in
     * the persistent storage; <code>false</code> otherwise
     */
    public boolean update(Competency competency) {
        return competencyDAO.update(competency);
    }

    /**
     * Retrieves the <code>Competency</code> object with given <code>id</code>
     * from a persistent storage. Returns the <code>Competency</code> object
     * with given <code>id</code> if it exists in the persistent storage;
     * <code>null</code> if the <code>Competency</code> object with given
     * <code>id</code> doesn't exist in the persistent storage or if the
     * operation fails.
     *
     * @param id the id which identifies the competency
     * @return the <code>Competency</code> object with given <code>id</code> if
     * it exists in the persistent storage, returns <code>null</code> if the
     * <code>Competency</code> object with given <code>id</code> doesn't exist
     * in the persistent storage or if the operation fails
     */
    public Competency get(int id) {
        return competencyDAO.get(id);
    }

    /**
     * Retrieves a <code>List</code> of <code>Competency</code> objects from a
     * persistent storage. Returns the <code>List</code> of
     * <code>Competency</code> objects if the operation is successful;
     * <code>null</code> otherwise.
     *
     * @return the <code>List</code> of <code>Competency</code> objects from the
     * persistent storage if the operation is successful; <code>null</code>
     * otherwise
     */
    public List<Competency> getAll() {
        Set<Competency> competencies = competencyDAO.getAll();
        return competencies != null ? new ArrayList<>(competencies) : null;
    }
}
