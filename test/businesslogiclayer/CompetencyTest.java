/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Competency class test.
 * 
 * @author carbo
 */
public class CompetencyTest {
    /**
     * Test of first constructor, with null description.
     */
    @Test(expected = NullPointerException.class)
    public void testFirstConstructorNull() {
        Competency competency = new Competency(null);
    }

    /**
     * Test of second constructor, with null description.
     */
    @Test(expected = NullPointerException.class)
    public void testSecondConstructorNull() {
        Competency competency = new Competency(1, null);
    }

    /**
     * Test of equality for hashCode method, of class Competency.
     */
    @Test
    public void testHashCodeEquality() {
        Competency competency1 = new Competency("empty description");
        Competency competency2 = new Competency(1, "empty description");
        assertEquals(competency1.hashCode(), competency2.hashCode());
    }

    /**
     * Test of inequality for hashCode method, of class Competency.
     */
    @Test
    public void testHashCodeInequality() {
        Competency competency1 = new Competency("empty");
        Competency competency2 = new Competency(1, "empty description");
        assertNotEquals(competency1.hashCode(), competency2.hashCode());
    }

    /**
     * Test of equality for equals method, of class Competency.
     */
    @Test
    public void testEqualsEquality() {
        Competency competency1 = new Competency("empty description");
        Competency competency2 = new Competency(1, "empty description");
        assertEquals(competency1, competency2);
    }
    
    /**
     * Test of inequality for equals method, of class Competency.
     */
    @Test
    public void testEqualsInequality() {
        Competency competency1 = new Competency("empty");
        Competency competency2 = new Competency(1, "empty description");
        assertNotEquals(competency1, competency2);
    }

}