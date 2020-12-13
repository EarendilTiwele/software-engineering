/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cypher class test.
 * 
 * @author carbo
 */
public class CypherTest {

    private Cypher cypher;

    public CypherTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        cypher = new Cypher();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of encode method, of class Cypher. Assert that encode and decode
     * methods work together.
     */
    @Test
    public void testEncodeDecode() {
        String word = "cypherTest_Word!*10";
        assertEquals(word, cypher.decode(cypher.encode(word)));
    }

}
