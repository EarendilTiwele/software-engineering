/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogiclayer;

import java.util.HashSet;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Procedure class test.
 * 
 * @author carbo
 */
public class ProcedureTest {

    private Procedure procedure;

    @Before
    public void setUp() {
        procedure = new Procedure("procedure", "./smp1.pdf");
    }

    /**
     * Test of first constructor, with null name.
     */
    @Test(expected = NullPointerException.class)
    public void testFirstConstructorNullName() {
        Procedure procedure = new Procedure(1, null, "");
    }

    /**
     * Test of first constructor, with null SMP path.
     */
    @Test(expected = NullPointerException.class)
    public void testFirstConstructorNullSMP() {
        Procedure procedure = new Procedure(1, "procedure name", null);
    }

    /**
     * Test of second constructor, with null name.
     */
    @Test(expected = NullPointerException.class)
    public void testSecondConstructorNullName() {
        Procedure procedure = new Procedure(null, "");
    }

    /**
     * Test of second constructor, with null SMP path.
     */
    @Test(expected = NullPointerException.class)
    public void testSecondConstructorNullSMP() {
        Procedure procedure = new Procedure("procedure name", null);
    }

    /**
     * Test of getCompetencies method (no competencies), of class Procedure.
     */
    @Test
    public void testGetCompetenciesEmpty() {
        Set<Competency> competencies = procedure.getCompetencies();
        assertTrue(competencies.isEmpty());
    }

    /**
     * Test of addCompetency method (one competency), of class Procedure.
     */
    @Test
    public void testAddCompetencyOne() {
        String description = "description1";
        Competency competency = new Competency(description);
        
        procedure.addCompetency(competency);
        
        Set<Competency> competencies = procedure.getCompetencies();
        
        Set<Competency> expectedCompetencies = new HashSet<>();
        expectedCompetencies.add(new Competency(description));
        
        assertEquals(expectedCompetencies, competencies);
    }

    /**
     * Test of addCompetency method (multiple competencies), of class Procedure.
     */
    @Test
    public void testAddCompetencyMultiple() {
        String description1 = "description1";
        String description2 = "description2";
        String description3 = "description3";

        Competency competency1 = new Competency(description1);
        Competency competency2 = new Competency(description2);
        Competency competency3 = new Competency(description3);

        procedure.addCompetency(competency1);
        procedure.addCompetency(competency2);
        procedure.addCompetency(competency3);

        Set<Competency> competencies = procedure.getCompetencies();

        assertEquals(3, competencies.size());
        assertTrue(competencies.contains(new Competency(description1)));
        assertTrue(competencies.contains(new Competency(description2)));
        assertTrue(competencies.contains(new Competency(description3)));

    }

}
