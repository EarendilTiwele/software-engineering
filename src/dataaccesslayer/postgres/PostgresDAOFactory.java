/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer.postgres;

import dataaccesslayer.ActivityDAO;
import dataaccesslayer.AssignmentDAO;
import dataaccesslayer.CompetencyDAO;
import dataaccesslayer.DAOFactory;
import dataaccesslayer.MaintainerSkillsDAO;
import dataaccesslayer.ProcedureDAO;
import dataaccesslayer.ProcedureSkillsDAO;
import dataaccesslayer.SiteDAO;
import dataaccesslayer.TypologyDAO;
import dataaccesslayer.UserDAO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author alexd
 */
public class PostgresDAOFactory extends DAOFactory {

    private static Connection connection;
    private static final String DBURL
            = "jdbc:postgresql://ec2-54-75-246-118.eu-west-1.compute.amazonaws.com/d4nuqe4269qu7k?sslmode=require";
    private static final Properties PROPS = new Properties();

    static {
        PROPS.setProperty("user", "kbhyahfpxyqabj");
        PROPS.setProperty("password", "7fe433219e2003f8119018667ac82205c6164d4d56b0ff5189cf25b1385a49eb");
        PROPS.setProperty("ssl", "true");
    }

    /**
     * Returns the connection to the Postgres Database.
     *
     * @return the connection to the Postgres Database
     * @throws SQLException if a database access error occurs
     */
    public static Connection createConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DBURL, PROPS);
        }
        return connection;
    }

    @Override
    public ActivityDAO getActivityDAO() {
        return new PostgresActivityDAO();
    }

    @Override
    public CompetencyDAO getCompetencyDAO() {
        return new PostgresCompetencyDAO();
    }

    @Override
    public ProcedureDAO getProcedureDAO() {
        return new PostgresProcedureDAO();
    }

    @Override
    public SiteDAO getSiteDAO() {
        return new PostgresSiteDAO();
    }

    @Override
    public TypologyDAO getTypologyDAO() {
        return new PostgresTypologyDAO();
    }

    @Override
    public UserDAO getUserDAO() {
        return new PostgresUserDAO();
    }

    @Override
    public AssignmentDAO getAssignmentDAO() {
        return new PostgresAssignmentDAO();
    }

    @Override
    public MaintainerSkillsDAO getMaintainerSkillsDAO() {
        return new PostgresMaintainerSkillsDAO();
    }

    @Override
    public ProcedureSkillsDAO getProcedureSkillsDAO() {
        return new PostgresProcedureSkillsDAO();
    }

}
