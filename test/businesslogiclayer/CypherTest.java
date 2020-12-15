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
     * Asserts that encode and decode methods work together with the specified
     * string.
     *
     * @param word the string to perform the test
     */
    private void testEncodeDecode(String word) {
        assertEquals(word, cypher.decode(cypher.encode(word)));
    }

    /**
     * Test of encode and decode methods, of class Cypher. Assert that encode
     * and decode methods work together with an empty string.
     */
    @Test
    public void testEncodeDecodeEmptyString() {
        testEncodeDecode("");
    }

    /**
     * Test of encode and decode methods, of class Cypher. Assert that encode
     * and decode methods work together with a one character (alphabetical)
     * string.
     */
    @Test
    public void testEncodeDecodeOneCharacterString() {
        String testChars = "abcdefghijklmnopqrstuvwxyz";
        testChars += testChars.toUpperCase();
        for (char c : testChars.toCharArray()) {
            testEncodeDecode(c + "");
        }
    }

    /**
     * Test of encode and decode methods, of class Cypher. Assert that encode
     * and decode methods work together with a one number string.
     */
    @Test
    public void testEncodeDecodeOneNumberString() {
        for (char c : "0123456789".toCharArray()) {
            testEncodeDecode(c + "");
        }
    }

    /**
     * Test of encode and decode methods, of class Cypher. Assert that encode
     * and decode methods work together with a one symbol string.
     */
    @Test
    public void testEncodeDecodeOneSymbolString() {
        for (char c : "+*/-|!£$%&/()=?^[]@#-_.:,;<>".toCharArray()) {
            testEncodeDecode(c + "");
        }
    }

    /**
     * Test of encode and decode methods, of class Cypher. Assert that encode
     * and decode methods work together with an alphanumeric string.
     */
    @Test
    public void testEncodeDecodeAlphanumericString() {
        testEncodeDecode("abcdef12345ABCDEF");
    }

    /**
     * Test of encode and decode methods, of class Cypher. Assert that encode
     * and decode methods work together with a string of symbols.
     */
    @Test
    public void testEncodeDecodeSymbolsString() {
        testEncodeDecode("+*/-|!£$%&/()=?^[]@#-_.:,;<>");
    }

}
