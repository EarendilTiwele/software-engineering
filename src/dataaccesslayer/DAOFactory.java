/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import dataaccesslayer.postgres.PostgresDAOFactory;

/**
 *
 * @author alexd
 */
public abstract class DAOFactory {

    // List of DAO types supported by the factory
    public static final int POSTGRES = 1;

    /* There will be a method for each DAO that can be 
    created. The concrete factories will have to 
    implement these methods. */
    public abstract ActivityDAO getActivityDAO();

    public abstract CompetencyDAO getCompetencyDAO();

    public abstract ProcedureDAO getProcedureDAO();

    public abstract SiteDAO getSiteDAO();

    public abstract TypologyDAO getTypologyDAO();

    public abstract UserDAO getUserDAO();

    public abstract AssignmentDAO getAssignmentDAO();

    public abstract MaintainerSkillsDAO getMaintainerSkillsDAO();

    public abstract ProcedureSkillsDAO getProcedureSkillsDAO();

    /**
     * Gets the DAO Factory specified by <code>whichFactory</code>.
     *
     * @param whichFactory the type of DAO Factory to be returned: POSTGRES
     * @return the specific DAO Factory
     */
    public static DAOFactory getDAOFactory(int whichFactory) {

        switch (whichFactory) {
            case POSTGRES:
                return new PostgresDAOFactory();
            default:
                return null;
        }
    }

}
