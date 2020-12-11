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

    public int insert(Competency competency) {
        return -1;
    }

    public boolean update(Competency competency) {
        return false;
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
