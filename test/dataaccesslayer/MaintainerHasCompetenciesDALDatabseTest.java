/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccesslayer;

import businesslogiclayer.Competency;
import businesslogiclayer.Maintainer;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author alexd
 */
public class MaintainerHasCompetenciesDALDatabseTest {
    
    private static MaintainerHasCompetenciesDALDatabase mhcDAL;
    private static Connection conn;
    private static final int ID = 1;
    private static final String DESCRIPTION = "test";
    
    public MaintainerHasCompetenciesDALDatabseTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws SQLException {
        mhcDAL = new MaintainerHasCompetenciesDALDatabase();
        conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(false);
        Statement stm = conn.createStatement();
        stm.executeUpdate("delete from competency where true;");
        stm.executeUpdate("delete from maintainerhascompetencies where true;");
        stm.executeUpdate(String.format("insert into competency (id, description)"
                                      + " values (%d, '%s');", ID, DESCRIPTION));  
        stm.executeUpdate(String.format("insert into competency (id, description)"
                                      + " values (%d, '%s');", ID + 1, DESCRIPTION));  
        stm.executeUpdate(String.format("insert into maintainerhascompetencies"
                                      + " (maintainerId, competencyId)"
                                      + " values (%d, %d);", ID, ID));  
        stm.executeUpdate(String.format("insert into maintainerhascompetencies"
                                      + " (maintainerId, competencyId)"
                                      + " values (%d, %d);", ID, ID + 1));  
    }
    
    @AfterClass
    public static void tearDownClass() throws SQLException {
        conn.rollback();
        conn.close();
    }
    
    /**
     * Create a set of local competencies with same data of the ones inserted in setUpClass
     * Create a maintainer with same id of the maintainerId put in setUpClass
     * Get the competencies from db and compare them with the local competencies
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetAllMaintainers () throws SQLException {
        Set<Competency> localCompetencies = new HashSet<>();
        localCompetencies.add(new Competency(ID, DESCRIPTION));
        localCompetencies.add(new Competency(ID + 1, DESCRIPTION));
        Maintainer maintainer = new Maintainer(ID, DESCRIPTION, DESCRIPTION);
        Set<Competency> dbCompetencies = mhcDAL.getAllCompetencies(maintainer);
        assertTrue(dbCompetencies.containsAll(localCompetencies));
    }
    
}
