/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import dataaccesslayer.postgres.PostgresCompetencyDAO;
import dataaccesslayer.postgres.PostgresDAOFactory;
import datatransferobjects.Competency;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author alexd
 */
public class CompetencyDALDatabaseTest {
    
    private static PostgresCompetencyDAO competencyDAL;
    private static Connection conn;
    private static final int ID = 1;
    private static final String DESCRIPTION = "test";
    
    public CompetencyDALDatabaseTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws SQLException {
        competencyDAL = new PostgresCompetencyDAO();
        conn = PostgresDAOFactory.createConnection();
        conn.setAutoCommit(false);
        Statement stm = conn.createStatement();
        stm.executeUpdate("delete from competency where true;");
        stm.executeUpdate(String.format("insert into competency (id, description)"
                                      + " values (%d, '%s');", ID, DESCRIPTION));  
    }
    
    @AfterClass
    public static void tearDownClass() throws SQLException {
        conn.rollback();
        conn.close();
    }
    
    /**
     * Create a local competency with same data of the one inserted in setUpClass
     * Get the competency from db and compare it with the local competency
     * @throws java.sql.SQLException
     */
    @Test
    public void testGet () throws SQLException {
        Competency localCompetency = new Competency(ID, DESCRIPTION);
        Competency dbCompetency = competencyDAL.get(1);
        assertEquals(localCompetency, dbCompetency);
        assertNull(competencyDAL.get(2));
    }
    
}
